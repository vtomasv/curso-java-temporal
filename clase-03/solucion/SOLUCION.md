# Solucionario Clase 03

## C03-E01 — Parser robusto

**Por qué:** Al procesar lotes de datos, un error en un registro no debe detener todo el proceso. Es fundamental atrapar excepciones específicas, registrar el contexto del error (línea, campo) y continuar.

```java
package com.sigeo.clase03;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public record ParseResult(List<String> validLines, List<ParseError> errors) {}
    public record ParseError(int lineNumber, String field, String cause) {}

    public ParseResult parse(List<String> lines) {
        List<String> validLines = new ArrayList<>();
        List<ParseError> errors = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            try {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Se esperaban 3 campos, se encontraron " + parts.length);
                }
                validLines.add(line);
            } catch (Exception e) {
                errors.add(new ParseError(i + 1, "formato", e.getMessage()));
            }
        }

        return new ParseResult(validLines, errors);
    }
}
```

## C03-E02 — Importador seguro

**Por qué:** `try-with-resources` garantiza que los recursos se cierren incluso si ocurre una excepción. Si tanto el bloque `try` como el método `close()` lanzan excepciones, la excepción de `close()` se añade como "suprimida" (suppressed) a la original, evitando que se pierda información vital para el debugging.

```java
package com.sigeo.clase03;

public class SafeImporter {

    public static class FailingResource implements AutoCloseable {
        private boolean closed = false;

        public void doWork() {
            throw new RuntimeException("Error durante el trabajo");
        }

        @Override
        public void close() throws Exception {
            if (closed) {
                throw new IllegalStateException("Recurso ya cerrado");
            }
            closed = true;
            throw new Exception("Error al cerrar el recurso");
        }

        public boolean isClosed() {
            return closed;
        }
    }

    public void importData(FailingResource resource) throws Exception {
        try (resource) {
            resource.doWork();
        }
    }
}
```

## C03-E03 — Trazabilidad de operación

**Por qué:** El MDC (Mapped Diagnostic Context) permite inyectar información contextual (como un ID de correlación) en todos los logs de un hilo, facilitando el rastreo de operaciones en sistemas concurrentes. Es crucial limpiar el MDC en un bloque `finally` para evitar que datos de una operación se filtren a otra si el hilo se reutiliza.

```java
package com.sigeo.clase03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class OperationTracker {
    private static final Logger logger = LoggerFactory.getLogger(OperationTracker.class);

    public void processOperation(String correlationId, String userId, String secretToken) {
        try {
            MDC.put("correlationId", correlationId);
            logger.info("Iniciando operación para el usuario: {}", userId);
            
            // Simulación de procesamiento costoso
            logger.debug("Procesando datos complejos...");
            
        } finally {
            MDC.remove("correlationId");
        }
    }
}
```

## C03-E04 — Error intermitente por límite

**Por qué:** El debugging efectivo requiere entender las condiciones de borde. En este caso, la lógica original causaba una división por cero cuando `basePriority` era 10 en el día 31. La solución es manejar este caso específico o cambiar la lógica matemática para evitar el cero.

```java
package com.sigeo.clase03;

import java.time.LocalDate;

public class PriorityCalculator {

    public int calculatePriority(int basePriority, LocalDate date) {
        int multiplier = 10;
        if (date.getDayOfMonth() == 31) {
            multiplier = 10 - basePriority;
        }
        
        if (multiplier == 0) {
            return 100; // Manejo del caso borde para evitar división por cero
        }
        
        return 1000 / multiplier;
    }
}
```

## C03-E05 — Transición inválida

**Por qué:** Las excepciones de bajo nivel (como `IllegalArgumentException`) a menudo carecen de contexto de negocio. Envolverlas en excepciones de dominio personalizadas mejora la legibilidad y el manejo de errores en capas superiores, pero es vital pasar la excepción original como `cause` para no perder el stack trace original.

```java
package com.sigeo.clase03;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

```java
package com.sigeo.clase03;

public class StateService {

    public void transitionState(String currentState, String newState) {
        try {
            performLowLevelTransition(currentState, newState);
        } catch (IllegalArgumentException e) {
            throw new InvalidStateTransitionException(
                "No se puede transicionar de " + currentState + " a " + newState, 
                e
            );
        }
    }

    private void performLowLevelTransition(String current, String next) {
        if ("FINAL".equals(current)) {
            throw new IllegalArgumentException("Estado final inmutable");
        }
    }
}
```

## C03-E06 — Repositorio inestable

**Por qué:** En sistemas distribuidos, los fallos transitorios son comunes. Implementar un patrón de reintento (retry) simple puede aumentar significativamente la resiliencia. Es importante limitar los reintentos para no sobrecargar el sistema y loguear cada intento fallido para observabilidad.

```java
package com.sigeo.clase03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResilientService {
    private static final Logger logger = LoggerFactory.getLogger(ResilientService.class);
    private final UnstableRepository repository;

    public ResilientService(UnstableRepository repository) {
        this.repository = repository;
    }

    public String getReliableData() {
        int maxRetries = 2;
        int attempt = 0;
        
        while (true) {
            try {
                attempt++;
                return repository.fetchData();
            } catch (RuntimeException e) {
                if (attempt > maxRetries) {
                    logger.error("Todos los intentos fallaron. Último error: {}", e.getMessage());
                    throw e;
                }
                logger.warn("Intento {} falló: {}. Reintentando...", attempt, e.getMessage());
            }
        }
    }
}
```

## C03-E07 — Fuga por listener

**Por qué:** Las fugas de memoria en Java a menudo ocurren por "referencias retenidas" (loitering objects). Si un objeto de larga vida (como `EventManager`) mantiene referencias a objetos de corta vida (los listeners) en una colección, el Garbage Collector no puede liberarlos. Proveer un método para desregistrar listeners es la solución clásica.

```java
package com.sigeo.clase03;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<EventListener> listeners = new ArrayList<>();

    public interface EventListener {
        void onEvent(String event);
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void fireEvent(String event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public int getListenerCount() {
        return listeners.size();
    }
}
```

```java
// En EventManagerTest.java, descomentar:
manager.unregisterListener(listener);
assertThat(manager.getListenerCount()).isEqualTo(0);
```

## C03-E08 — Postmortem mínimo

**Por qué:** Un buen postmortem se centra en los hechos, identifica la causa raíz sin culpar a las personas, y propone acciones preventivas concretas.

*(Ejemplo de contenido para postmortem.md)*
```markdown
# Postmortem: Fallo en procesamiento de lote nocturno

**Hechos:**
- A las 02:15 AM, el proceso de importación falló tras procesar 5000 registros.
- Los logs muestran un `OutOfMemoryError` seguido de la caída del servicio.
- El servicio se reinició automáticamente a las 02:20 AM.

**Hipótesis/Causa Raíz:**
- El parser CSV estaba acumulando todos los registros fallidos en memoria junto con el contenido completo del archivo original, excediendo el heap disponible.

**Acciones Preventivas (Mejoras de Logging/Diseño):**
1. Implementar procesamiento por streaming (línea a línea) en lugar de cargar todo el archivo.
2. Añadir métricas de uso de memoria al log cada 1000 registros procesados.
3. Configurar alertas para uso de heap > 85%.
4. Limitar el tamaño máximo del archivo de entrada aceptado.
5. Incluir el `correlationId` del lote en todos los logs para facilitar el rastreo.
```
