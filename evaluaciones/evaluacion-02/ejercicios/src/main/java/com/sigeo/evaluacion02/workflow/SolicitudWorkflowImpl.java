package com.sigeo.evaluacion02.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class SolicitudWorkflowImpl implements SolicitudWorkflow {

    private final SolicitudActivities activities = Workflow.newActivityStub(
            SolicitudActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    // TODO(EV02-E09): Agregar Schedule-to-Close y RetryOptions:
                    // máximo 3 intentos, backoff inicial de 100 ms y VALIDATION sin retry.
                    .build()
    );

    @Override
    public SolicitudWorkflowResult procesar(SolicitudWorkflowInput input) {
        // TODO(EV02-E10): Orquestar registrar y notificar mediante Activities.
        // No use reloj del sistema, UUID, threads, HTTP, archivos ni base de datos aquí.
        return new SolicitudWorkflowResult("TODO", "PENDIENTE", "TODO EV02-E10");
    }
}
