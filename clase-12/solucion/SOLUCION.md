# Solución de Ejercicios - Clase 12

## C12-E01 — Mapa de pasos

**Por qué:** Es fundamental definir claramente qué acciones componen la saga y cuáles son sus compensaciones semánticas (no un simple rollback de base de datos, sino una acción de negocio que anule el efecto).

```java
package com.sigeo.clase12;

import java.util.List;

public class SagaSteps {

    public record StepDefinition(String name, String action, String compensation) {}

    public List<StepDefinition> getSagaSteps() {
        return List.of(
            new StepDefinition("Reserva", "Reservar recurso", "Cancelar reserva de recurso"),
            new StepDefinition("Presupuesto", "Asignar presupuesto", "Liberar presupuesto"),
            new StepDefinition("Agenda", "Agendar operación", "Cancelar agenda"),
            new StepDefinition("Notificación", "Enviar notificación", "Enviar notificación de cancelación")
        );
    }
}
```

## C12-E02 — Saga mínima

**Por qué:** Temporal provee la clase `Saga` para registrar compensaciones. Si un paso falla, se llama a `saga.compensate()` para ejecutar las compensaciones en orden inverso.

```java
// En SagaWorkflowImpl.java
@Override
public String executeSaga(String reservationId, boolean failAtBudget, boolean failAtAgenda, boolean failAtNotification) {
    Saga saga = new Saga(new Saga.Options.Builder().setParallelCompensation(false).build());
    try {
        activities.reserveResource(reservationId);
        saga.addCompensation(activities::cancelResource, reservationId);

        activities.allocateBudget(reservationId, failAtBudget);
        saga.addCompensation(activities::releaseBudget, reservationId);
        
        status = "COMPLETED";
        return status;
    } catch (Exception e) {
        status = "COMPENSATING";
        saga.compensate();
        status = "COMPENSATED";
        throw e;
    }
}
```

## C12-E03 — Fallo por etapa

**Por qué:** Al parametrizar los fallos, podemos probar que la saga se detiene en el punto exacto de fallo y compensa solo lo que ya se había ejecutado exitosamente.

```java
// En SagaWorkflowImpl.java (continuación)
        activities.scheduleAgenda(reservationId, failAtAgenda);
        saga.addCompensation(activities::cancelAgenda, reservationId);

        activities.sendNotification(reservationId, failAtNotification);
        // No hay compensación para notificación si es el último paso
```

## C12-E04 — Compensación inestable

**Por qué:** Las compensaciones también pueden fallar. Deben tener políticas de reintento robustas (incluso infinitas) para asegurar que el sistema eventualmente alcance un estado consistente.

```java
// En SagaWorkflowImpl.java
    private final ActivityOptions compensationOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .setRetryOptions(RetryOptions.newBuilder()
                    .setMaximumAttempts(10) // Más reintentos para compensaciones
                    .build())
            .build();
    private final SagaActivities compensationActivities = Workflow.newActivityStub(SagaActivities.class, compensationOptions);

// Usar compensationActivities en saga.addCompensation
```

## C12-E05 — Doble cancelación

**Por qué:** Las compensaciones deben ser idempotentes porque Temporal puede reintentarlas si hay fallos de red o caídas del worker.

```java
// En SagaActivitiesImpl.java
    @Override
    public void cancelResource(String reservationId) {
        if (cancelledResources.add(reservationId)) {
            System.out.println("Canceling resource for " + reservationId);
        } else {
            System.out.println("Resource already canceled for " + reservationId + ", ignoring.");
        }
    }
```

## C12-E06 — Endpoint de operación

**Por qué:** El cliente HTTP no debe bloquearse esperando que termine una saga larga. Se inicia asíncronamente y se provee un endpoint para consultar el estado.

```java
// En SagaController.java
    @PostMapping
    public Map<String, String> startSaga(@RequestParam(defaultValue = "false") boolean failAtBudget) {
        String workflowId = "saga-" + UUID.randomUUID().toString();
        SagaWorkflow workflow = workflowClient.newWorkflowStub(SagaWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue("SAGA_TASK_QUEUE")
                        .build());
        
        WorkflowClient.start(workflow::executeSaga, workflowId, failAtBudget, false, false);
        return Map.of("workflowId", workflowId, "status", "STARTED");
    }

    @GetMapping("/{workflowId}")
    public Map<String, String> getSagaStatus(@PathVariable String workflowId) {
        SagaWorkflow workflow = workflowClient.newWorkflowStub(SagaWorkflow.class, workflowId);
        return Map.of("workflowId", workflowId, "status", workflow.getStatus());
    }
```

## C12-E07 — Contextos separados

**Por qué:** Para dominios complejos, es mejor delegar partes de la saga a Child Workflows que encapsulan su propia lógica y estado.

```java
// En SagaWorkflowImpl.java
    @Override
    public String executeSaga(String reservationId, boolean failAtBudget, boolean failAtAgenda, boolean failAtNotification) {
        ResourceChildWorkflow child = Workflow.newChildWorkflowStub(ResourceChildWorkflow.class);
        child.processResource(reservationId);
        // ...
    }
```

## C12-E08 — Evento de completitud

**Por qué:** El patrón Outbox asegura que la actualización de la base de datos local y la publicación del evento ocurran de forma atómica.

```java
// En OutboxService.java
    @Transactional
    public void saveEvent(String eventId, String payload) {
        // Guardar en tabla outbox
        // jdbcTemplate.update("INSERT INTO outbox (id, payload) VALUES (?, ?)", eventId, payload);
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {
        // Leer de outbox, publicar en RabbitMQ/Kafka, y marcar como procesado
    }
```
