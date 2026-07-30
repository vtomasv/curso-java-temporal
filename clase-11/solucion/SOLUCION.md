# Solución de Ejercicios - Clase 11

## C11-E01 — Aprobar o rechazar
**Por qué:** Las señales (Signals) permiten enviar datos asíncronos a un Workflow en ejecución. Usamos variables de estado para guardar la decisión.

```java
@Override
public void approve(String commandId) {
    if (this.decision == null) {
        this.decision = "APPROVED";
        this.lastCommandId = commandId;
    }
}

@Override
public void reject(String commandId, String reason) {
    if (this.decision == null) {
        this.decision = "REJECTED";
        this.rejectionReason = reason;
        this.lastCommandId = commandId;
    }
}
```

## C11-E02 — Estado consultable
**Por qué:** Las consultas (Queries) permiten leer el estado interno del Workflow sin mutarlo ni generar eventos en el historial.

```java
@Override
public ApprovalState getState() {
    return new ApprovalState(this.decision, this.rejectionReason, this.priority);
}
```

## C11-E03 — Vencimiento automático
**Por qué:** `Workflow.await` bloquea la ejecución hasta que una condición sea verdadera o se alcance un timeout. Es la forma correcta de esperar en Temporal, nunca `Thread.sleep`.

```java
boolean acted = Workflow.await(Duration.ofMinutes(30), () -> this.decision != null);
if (!acted) {
    this.decision = "TIMEOUT";
}
```

## C11-E04 — Cambiar prioridad confirmado
**Por qué:** Los Updates permiten enviar datos, validarlos antes de aceptarlos y retornar un resultado síncrono al cliente.

```java
@UpdateValidatorMethod
public void validateUpdatePriority(int newPriority) {
    if (newPriority < 1 || newPriority > 5) {
        throw new IllegalArgumentException("Prioridad debe estar entre 1 y 5");
    }
    if (this.decision != null) {
        throw new IllegalStateException("No se puede cambiar prioridad de una solicitud ya decidida");
    }
}

@Override
public int updatePriority(int newPriority) {
    this.priority = newPriority;
    return this.priority;
}
```

## C11-E05 — Comando repetido
**Por qué:** Para evitar procesar el mismo comando dos veces (por reintentos de red), guardamos los IDs de los comandos procesados.

```java
private final Set<String> processedCommands = new HashSet<>();

@Override
public void approve(String commandId) {
    if (processedCommands.contains(commandId)) {
        return; // Deduplicación
    }
    if (this.decision == null) {
        this.decision = "APPROVED";
        processedCommands.add(commandId);
    }
}
```

## C11-E06 — Revisión especializada
**Por qué:** Los Child Workflows permiten modularizar la lógica y distribuir el historial. Se configuran con opciones como `ParentClosePolicy`.

```java
ChildWorkflowOptions options = ChildWorkflowOptions.newBuilder()
    .setWorkflowId(Workflow.getInfo().getWorkflowId() + "-review")
    .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_TERMINATE)
    .build();
TechnicalReviewWorkflow reviewWorkflow = Workflow.newChildWorkflowStub(TechnicalReviewWorkflow.class, options);
String reviewResult = reviewWorkflow.performReview(requestDetails);
```

## C11-E07 — Bandeja de eventos larga
**Por qué:** Cuando un Workflow procesa muchos eventos, su historial crece demasiado. `ContinueAsNew` reinicia el historial pasando el estado actual a una nueva ejecución.

```java
@Override
public void processEvents(int count) {
    for (int i = 0; i < 50; i++) {
        Workflow.await(() -> !eventQueue.isEmpty());
        String event = eventQueue.poll();
        this.processedCount++;
    }
    // Continuar como nuevo después de 50 eventos
    Workflow.continueAsNew(this.processedCount);
}
```

## C11-E08 — Aprobación vs expiración
**Por qué:** Si una señal llega casi al mismo tiempo que un timer expira, el orden de evaluación en `Workflow.await` determina el resultado. Temporal garantiza determinismo.

```java
// Si la señal llega en el mismo milisegundo que el timer, 
// la condición this.decision != null se evalúa primero.
boolean acted = Workflow.await(Duration.ofMinutes(30), () -> this.decision != null);
if (!acted) {
    this.decision = "TIMEOUT";
}
// El resultado es consistente: si hay decisión, no es TIMEOUT.
```
