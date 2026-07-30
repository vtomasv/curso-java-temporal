# Solución Clase 18: Examen final teórico-práctico y defensa del proyecto

Esta clase corresponde al examen final. Las soluciones exactas dependerán del proyecto integrador desarrollado por cada alumno, pero a continuación se presentan ejemplos de cómo abordar cada ejercicio práctico.

## C18-E01 — Análisis de arquitectura
**Por qué:** Evalúa la comprensión teórica profunda de los conceptos aplicados durante el curso.
**Solución:** El alumno debe entregar una hoja con las respuestas a los 12 ítems, justificando sus decisiones y detectando antipatrones. No hay código asociado.

## C18-E02 — Cambio de regla
**Por qué:** Evalúa la capacidad de modificar el dominio, la persistencia y la capa web de forma segura.
**Solución:**
```java
// Ejemplo de implementación en el dominio
@Entity
public class Solicitud {
    // ...
    @Column(name = "prioridad")
    private Prioridad prioridad;
    
    public void actualizarPrioridad(Prioridad nuevaPrioridad) {
        if (this.estado == Estado.COMPLETADA) {
            throw new IllegalStateException("No se puede cambiar la prioridad de una solicitud completada");
        }
        this.prioridad = nuevaPrioridad;
    }
}

// Ejemplo de controlador HTTP
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    
    @PatchMapping("/{id}/prioridad")
    public ResponseEntity<Void> cambiarPrioridad(@PathVariable Long id, @RequestBody PrioridadRequest request) {
        solicitudService.cambiarPrioridad(id, request.prioridad());
        return ResponseEntity.ok().build();
    }
}
```

## C18-E03 — Nueva interacción
**Por qué:** Evalúa el manejo de Temporal, específicamente la adición de Signals o Updates manteniendo el determinismo.
**Solución:**
```java
// Ejemplo de interfaz de Workflow
@WorkflowInterface
public interface OperacionWorkflow {
    @WorkflowMethod
    void iniciarOperacion(OperacionRequest request);
    
    @SignalMethod
    void cancelarOperacion(String motivo);
    
    @UpdateMethod
    EstadoOperacion consultarEstado();
}

// Ejemplo de implementación
public class OperacionWorkflowImpl implements OperacionWorkflow {
    private boolean cancelada = false;
    private String motivoCancelacion;
    
    @Override
    public void iniciarOperacion(OperacionRequest request) {
        // Lógica del workflow
        Workflow.await(() -> cancelada || operacionCompletada());
        if (cancelada) {
            // Lógica de compensación
        }
    }
    
    @Override
    public void cancelarOperacion(String motivo) {
        this.cancelada = true;
        this.motivoCancelacion = motivo;
    }
}
```

## C18-E04 — Fallo inducido
**Por qué:** Evalúa la capacidad de diagnosticar y corregir problemas de resiliencia, como retries infinitos o falta de idempotencia.
**Solución:**
```java
// Ejemplo de corrección de Activity Options
ActivityOptions options = ActivityOptions.newBuilder()
    .setStartToCloseTimeout(Duration.ofSeconds(10))
    .setRetryOptions(RetryOptions.newBuilder()
        .setInitialInterval(Duration.ofSeconds(1))
        .setMaximumInterval(Duration.ofSeconds(10))
        .setBackoffCoefficient(2.0)
        .setMaximumAttempts(5) // Corregido: antes era infinito o muy alto
        .setDoNotRetry(IllegalArgumentException.class.getName()) // Corregido: no reintentar errores de negocio
        .build())
    .build();
```

## C18-E05 — Demo resiliente
**Por qué:** Demuestra que el sistema puede recuperarse de fallos reales.
**Solución:** El alumno debe realizar una demostración en vivo, por ejemplo, apagando la base de datos o un worker de Temporal durante la ejecución de un workflow, y mostrando cómo el sistema se recupera al restaurar el servicio.

## C18-E06 — Preguntas técnicas
**Por qué:** Asegura que el alumno comprende el código que ha escrito o modificado.
**Solución:** Evaluación oral.

## C18-E07 — Higiene de entrega
**Por qué:** Fomenta buenas prácticas de desarrollo y seguridad.
**Solución:** Revisión del repositorio para asegurar que no hay secretos hardcodeados, que el build es reproducible y que las pruebas pasan.

## C18-E08 — Limitaciones y roadmap
**Por qué:** Fomenta la reflexión crítica sobre el trabajo realizado.
**Solución:** El alumno debe entregar un documento o nota final detallando 3 limitaciones reales de su sistema y 3 mejoras priorizadas para el futuro.
