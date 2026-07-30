# Soluciones - Clase 13

## C13-E01 — Vencimiento en segundos

**Por qué:** Para probar workflows de larga duración (ej. 30 días) sin esperar ese tiempo, Temporal provee `TestWorkflowEnvironment` que implementa "time skipping". El tiempo avanza automáticamente cuando el workflow está bloqueado esperando.

```java
// VencimientoWorkflowImpl.java
package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import java.time.Duration;

public class VencimientoWorkflowImpl implements VencimientoWorkflow {
    @Override
    public String procesarConVencimiento(int dias) {
        // El workflow se bloquea aquí, pero en el test el tiempo saltará
        Workflow.sleep(Duration.ofDays(dias));
        return "Vencido tras " + dias + " días";
    }
}

// VencimientoWorkflowTest.java
package com.sigeo.clase13;

import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VencimientoWorkflowTest {
    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private VencimientoWorkflow workflow;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("VENCIMIENTO_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(VencimientoWorkflowImpl.class);
        testEnv.start();
        
        workflow = testEnv.getWorkflowClient().newWorkflowStub(
            VencimientoWorkflow.class,
            io.temporal.client.WorkflowOptions.newBuilder()
                .setTaskQueue("VENCIMIENTO_TASK_QUEUE")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testVencimientoTimeSkipping() {
        // Este test tomará milisegundos aunque el workflow duerma 30 días
        String resultado = workflow.procesarConVencimiento(30);
        assertThat(resultado).isEqualTo("Vencido tras 30 días");
    }
}
```

## C13-E02 — Signals y Updates

**Por qué:** Las señales permiten enviar datos a un workflow en ejecución de forma asíncrona, mientras que los updates permiten interacción síncrona con validación.

```java
// InteraccionWorkflowImpl.java
package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import java.util.ArrayList;
import java.util.List;

public class InteraccionWorkflowImpl implements InteraccionWorkflow {
    private final List<String> eventos = new ArrayList<>();
    private boolean completado = false;

    @Override
    public List<String> ejecutar() {
        Workflow.await(() -> completado);
        return eventos;
    }

    @Override
    public void agregarEvento(String evento) {
        eventos.add(evento);
    }

    @Override
    public String actualizarEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            throw new IllegalArgumentException("Estado inválido");
        }
        eventos.add("Estado actualizado a: " + nuevoEstado);
        return "OK";
    }

    @Override
    public void completar() {
        completado = true;
    }
    
    @Override
    public List<String> consultarEventos() {
        return eventos;
    }
}
```

## C13-E03 — Historia incompatible

**Por qué:** Cuando cambiamos el código de un workflow (ej. añadiendo una nueva activity o cambiando el orden), las ejecuciones en curso fallarán con un error de no-determinismo al hacer replay de su historia.

```java
// ReplayWorkflowTest.java
package com.sigeo.clase13;

import io.temporal.testing.WorkflowReplayer;
import org.junit.jupiter.api.Test;
import java.io.File;

class ReplayWorkflowTest {
    @Test
    void testReplayHistoriaIncompatible() throws Exception {
        // Esto fallará si el código actual no coincide con la historia
        // WorkflowReplayer.replayWorkflowExecution(
        //     new File("src/test/resources/historia_incompatible.json"),
        //     ReplayWorkflowImpl.class
        // );
        
        // Para arreglarlo, debemos usar Workflow.getVersion() en el código
        // de producción para mantener compatibilidad hacia atrás.
    }
}
```

## C13-E04 — Nueva validación

**Por qué:** Para hacer cambios incompatibles de forma segura, usamos `Workflow.getVersion()`. Esto permite que las ejecuciones antiguas sigan la ruta de código original, mientras que las nuevas usan la nueva lógica.

```java
// VersionadoWorkflowImpl.java
package com.sigeo.clase13;

import io.temporal.workflow.Workflow;

public class VersionadoWorkflowImpl implements VersionadoWorkflow {
    @Override
    public String procesar() {
        // El versionado permite cambiar la lógica sin romper ejecuciones en curso
        int version = Workflow.getVersion("ValidacionExtra", Workflow.DEFAULT_VERSION, 1);
        
        if (version == Workflow.DEFAULT_VERSION) {
            return "Procesado sin validación extra";
        } else {
            return "Procesado CON validación extra";
        }
    }
}
```

## C13-E05 — Search Attributes operativos

**Por qué:** Los Search Attributes permiten indexar workflows en Elasticsearch/OpenSearch para poder buscarlos eficientemente por metadatos de negocio.

```java
// SearchAttributesWorkflowImpl.java
package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import java.util.HashMap;
import java.util.Map;

public class SearchAttributesWorkflowImpl implements SearchAttributesWorkflow {
    @Override
    public void ejecutarConAtributos(String responsable, String prioridad) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("Responsable", responsable);
        attributes.put("Prioridad", prioridad);
        attributes.put("Estado", "EN_PROGRESO");
        
        Workflow.upsertSearchAttributes(attributes);
        
        // Lógica del workflow...
        Workflow.sleep(java.time.Duration.ofMinutes(5));
        
        Map<String, Object> finalAttributes = new HashMap<>();
        finalAttributes.put("Estado", "COMPLETADO");
        Workflow.upsertSearchAttributes(finalAttributes);
    }
}
```

## C13-E06 — Correlación end-to-end

**Por qué:** Para rastrear una petición a través de múltiples servicios y workflows, necesitamos propagar un ID de correlación. En Temporal, esto se hace a través del contexto o MDC en los logs.

```java
// CorrelacionWorkflowImpl.java
package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class CorrelacionWorkflowImpl implements CorrelacionWorkflow {
    // Usar el logger de Temporal que es seguro para replay (no duplica logs)
    private static final Logger logger = Workflow.getLogger(CorrelacionWorkflowImpl.class);

    @Override
    public void procesar(String correlationId) {
        // En un entorno real, usaríamos interceptores para propagar el MDC
        logger.info("Iniciando procesamiento con correlationId: {}", correlationId);
        
        // Llamada a activity...
        
        logger.info("Finalizando procesamiento con correlationId: {}", correlationId);
    }
}
```

## C13-E07 — Configuración sin secretos

**Por qué:** Las credenciales y configuraciones sensibles nunca deben estar hardcodeadas. Spring Boot permite externalizarlas fácilmente.

```yaml
# application.yml
spring:
  application:
    name: sigeo-worker
  temporal:
    connection:
      target: ${TEMPORAL_TARGET:127.0.0.1:7233}
    namespace: ${TEMPORAL_NAMESPACE:default}
    workers:
      - task-queue: SIGEO_TASK_QUEUE
        workflow-classes:
          - com.sigeo.clase13.VencimientoWorkflowImpl
```

```env
# .env.example
TEMPORAL_TARGET=127.0.0.1:7233
TEMPORAL_NAMESPACE=default
# Para cloud:
# TEMPORAL_TARGET=namespace.tmprl.cloud:7233
# TEMPORAL_NAMESPACE=namespace
# TEMPORAL_MTLS_CERT_PATH=/path/to/cert.pem
# TEMPORAL_MTLS_KEY_PATH=/path/to/key.pem
```

## C13-E08 — History hygiene

**Por qué:** El historial de Temporal se guarda en base de datos. Guardar payloads muy grandes o datos sensibles (PII) es un problema de rendimiento y seguridad.

```markdown
# Auditoría de Datos (audit.md)

1. **Payloads excesivos:** En lugar de pasar el documento completo (PDF de 5MB) como argumento al workflow, pasar solo el ID del documento o la URL de S3. La Activity se encargará de descargarlo.
2. **Datos sensibles (PII):** Usar Data Converters (Payload Codecs) para encriptar los datos antes de que se guarden en el historial de Temporal.
3. **Search Attributes:** No usar Search Attributes para datos que cambian constantemente o que son muy grandes. Usar Memo para datos que no necesitan ser buscados pero sí mostrados en la UI.
```
