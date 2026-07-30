# Soluciones Clase 19

## C19-E01 — Análisis equivalente
**Por qué:** El análisis equivalente requiere evaluar una matriz de ítems. La solución debe implementar la lógica de evaluación.

```java
package com.sigeo.clase19;

public class AnalisisEquivalente {

    public String analizar(String[][] matriz) {
        // Implementación de la lógica de análisis
        return "Análisis completado";
    }
}
```

## C19-E02 — Cambio compatible
**Por qué:** Agregar un nuevo atributo requiere actualizar la entidad y asegurar que las validaciones y seguridad se mantengan.

```java
package com.sigeo.clase19;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "cambios")
public class CambioCompatible {

    @Id
    private Long id;
    
    private String descripcion;
    
    @Column(name = "nuevo_atributo")
    private String nuevoAtributo;
    
    // Getters y setters
}
```

## C19-E03 — Cancelación o compensación
**Por qué:** La cancelación segura en Temporal requiere manejar las señales de cancelación adecuadamente.

```java
package com.sigeo.clase19;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CancelacionCompensacion {

    @WorkflowMethod
    void ejecutarProceso(String id);
}
```

## C19-E04 — Nondeterminism o duplicate effect
**Por qué:** En Temporal, no se puede usar `UUID.randomUUID()` directamente en un Workflow porque causa no determinismo. Se debe usar `Workflow.randomUUID()`.

```java
package com.sigeo.clase19;

import io.temporal.workflow.Workflow;
import java.util.UUID;

public class Nondeterminism {

    public String generarId() {
        return Workflow.randomUUID().toString();
    }
}
```

## C19-E06 — Errores comunes
**Por qué:** Los catch vacíos ocultan errores. `@Transactional` no funciona en métodos privados. Las llamadas HTTP no deben hacerse directamente en un Workflow.

```java
package com.sigeo.clase19;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErroresComunes {

    private static final Logger logger = LoggerFactory.getLogger(ErroresComunes.class);

    public void procesarDatos() {
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            logger.error("Error al procesar datos", e);
            throw new RuntimeException("Error al procesar datos", e);
        }
    }
    
    @Transactional
    public void metodoPublico() {
        // Operación de base de datos
    }
    
    public void ejecutarWorkflow() {
        // Las llamadas HTTP deben hacerse en una Activity, no en el Workflow
    }
}
```
