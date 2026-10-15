package com.sigeo.evaluacion02.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class SolicitudWorkflowImpl implements SolicitudWorkflow {

    private final SolicitudActivities activities = Workflow.newActivityStub(
            SolicitudActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    .setScheduleToCloseTimeout(Duration.ofSeconds(20))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(3)
                            .setInitialInterval(Duration.ofMillis(100))
                            .setBackoffCoefficient(2.0)
                            .setDoNotRetry("VALIDATION")
                            .build())
                    .build()
    );

    @Override
    public SolicitudWorkflowResult procesar(SolicitudWorkflowInput input) {
        String solicitudId = activities.registrar(input);
        activities.notificar(solicitudId);
        return new SolicitudWorkflowResult(solicitudId, "REGISTRADA", "Solicitud registrada y notificada");
    }
}
