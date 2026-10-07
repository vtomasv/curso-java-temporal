# Soluciones Clase 10

## C10-E01 — Actividad HTTP acotada

**Por qué:** Es fundamental configurar un `StartToCloseTimeout` para evitar que una actividad se quede bloqueada indefinidamente si el servicio externo no responde. Además, limitamos los reintentos para este ejercicio específico.

```java
// HttpWorkflowImpl.java
package com.sigeo.clase10.e01;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class HttpWorkflowImpl implements HttpWorkflow {

    private final HttpActivity activity = Workflow.newActivityStub(HttpActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(2))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(1)
                            .build())
                    .build());

    @Override
    public String executeCall(int latencySeconds) {
        return activity.callExternalService(latencySeconds);
    }
}
```

## C10-E02 — Servicio 503 temporal

**Por qué:** Los errores transitorios (como 503) deben reintentarse, pero los errores permanentes (como 400 Bad Request) no tienen sentido reintentarlos porque siempre fallarán. Usamos `setDoNotRetry` para clasificar el 400.

```java
// ServiceWorkflowImpl.java
package com.sigeo.clase10.e02;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class ServiceWorkflowImpl implements ServiceWorkflow {

    private final ServiceActivity activity = Workflow.newActivityStub(ServiceActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofMillis(100))
                            .setBackoffCoefficient(2.0)
                            .setMaximumInterval(Duration.ofSeconds(1))
                            .setMaximumAttempts(5)
                            .setDoNotRetry("400")
                            .build())
                    .build());

    @Override
    public String executeService(String input) {
        return activity.processRequest(input);
    }
}
```

La Activity consulta `Activity.getExecutionContext().getInfo().getAttempt()` en lugar de mantener un contador local. Así el tercer intento sigue siendo reconocible aunque el Worker cambie entre reintentos.

## C10-E03 — ApplicationFailure tipada

**Por qué:** Usar `ApplicationFailure` con tipos específicos permite al Workflow tomar decisiones basadas en el tipo de error sin tener que parsear el mensaje de error, lo cual es frágil.

```java
// TypedFailureActivityImpl.java
package com.sigeo.clase10.e03;

import io.temporal.failure.ApplicationFailure;

public class TypedFailureActivityImpl implements TypedFailureActivity {
    @Override
    public void validateData(String data) {
        if ("invalid".equals(data)) {
            throw ApplicationFailure.newNonRetryableFailure("Validation failed", "VALIDATION");
        } else if ("missing".equals(data)) {
            throw ApplicationFailure.newNonRetryableFailure("Data not found", "NOT_FOUND");
        } else if ("down".equals(data)) {
            throw ApplicationFailure.newNonRetryableFailure("Provider is down", "PROVIDER_UNAVAILABLE");
        }
    }
}

// TypedFailureWorkflowImpl.java
package com.sigeo.clase10.e03;

import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class TypedFailureWorkflowImpl implements TypedFailureWorkflow {

    private final TypedFailureActivity activity = Workflow.newActivityStub(TypedFailureActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(2))
                    .build());

    @Override
    public String process(String data) {
        try {
            activity.validateData(data);
            return "Success";
        } catch (ActivityFailure e) {
            if (e.getCause() instanceof ApplicationFailure failure) {
                return switch (failure.getType()) {
                    case "VALIDATION" -> "Validation Error";
                    case "NOT_FOUND" -> "Not Found Error";
                    case "PROVIDER_UNAVAILABLE" -> "Provider Error";
                    default -> throw e;
                };
            }
            throw e;
        }
    }
}
```

## C10-E04 — Reserva única

**Por qué:** Las actividades en Temporal tienen semántica "at-least-once". Si una actividad falla después de realizar su efecto secundario pero antes de reportar el éxito, Temporal la reintentará. La idempotencia asegura que el efecto secundario no se duplique.

```java
// ReservationActivityImpl.java
package com.sigeo.clase10.e04;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReservationActivityImpl implements ReservationActivity {
    
    private final Map<String, String> reservations = new ConcurrentHashMap<>();
    private int callCount = 0;

    @Override
    public String makeReservation(String itemId, String idempotencyKey) {
        callCount++;
        
        return reservations.computeIfAbsent(idempotencyKey, ignored -> "RES-" + itemId);
    }

    public int getCallCount() {
        return callCount;
    }
}
```

## C10-E05 — Procesamiento por páginas

**Por qué:** Para actividades largas, los heartbeats sirven para dos propósitos: 1) avisar a Temporal que la actividad sigue viva (evitando timeouts prematuros) y 2) guardar el progreso (checkpointing) para que, en caso de fallo, el reintento pueda reanudar desde donde se quedó.

```java
// BatchProcessingActivityImpl.java
package com.sigeo.clase10.e05;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import java.util.Optional;

public class BatchProcessingActivityImpl implements BatchProcessingActivity {

    private boolean simulateCrash = true;

    @Override
    public int processBatch(int totalRecords) {
        ActivityExecutionContext context = Activity.getExecutionContext();
        
        int startOffset = context.getHeartbeatDetails(Integer.class).orElse(0);
        
        int processed = startOffset;
        
        for (int i = startOffset; i < totalRecords; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            
            processed++;
            
            if (processed % 100 == 0) {
                context.heartbeat(processed);
            }
            
            if (simulateCrash && processed == 500) {
                simulateCrash = false;
                throw new RuntimeException("Simulated crash at 500");
            }
        }
        
        return processed;
    }
}
```

## C10-E06 — Cancelar exportación

**Por qué:** La cancelación en Temporal es cooperativa. La actividad debe emitir heartbeats para recibir la señal de cancelación (que se manifiesta como una `InterruptedException` si está bloqueada, o se puede verificar explícitamente). El bloque `finally` asegura que los recursos se limpien.

```java
// ExportActivityImpl.java
package com.sigeo.clase10.e06;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;

public class ExportActivityImpl implements ExportActivity {

    private boolean cleanupCalled = false;

    @Override
    public void exportData() {
        ActivityExecutionContext context = Activity.getExecutionContext();
        
        try {
            for (int i = 0; i < 100; i++) {
                context.heartbeat(i);
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted", e);
                }
            }
        } finally {
            cleanupCalled = true;
        }
    }

    public boolean isCleanupCalled() {
        return cleanupCalled;
    }
}
```

## C10-E07 — Intento y latencia

**Por qué:** Loguear el contexto de la actividad (Workflow ID, Activity ID, Intento) es crucial para la observabilidad y el debugging en sistemas distribuidos. Nunca se deben loguear datos sensibles.

```java
// LoggingActivityImpl.java
package com.sigeo.clase10.e07;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingActivityImpl implements LoggingActivity {

    private static final Logger log = LoggerFactory.getLogger(LoggingActivityImpl.class);
    @Override
    public void doWork(String sensitiveData) {
        ActivityInfo info = Activity.getExecutionContext().getInfo();
        log.info("Executing activity. WorkflowId: {}, ActivityId: {}, Attempt: {}", 
                info.getWorkflowId(), info.getActivityId(), info.getAttempt());
        
        if (info.getAttempt() < 2) {
            throw new RuntimeException("Simulated transient error");
        }
    }
}
```
