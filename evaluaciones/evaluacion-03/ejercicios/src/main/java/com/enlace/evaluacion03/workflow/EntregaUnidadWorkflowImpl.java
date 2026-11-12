package com.enlace.evaluacion03.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class EntregaUnidadWorkflowImpl implements EntregaUnidadWorkflow {

    private final DespachoActivities activities = Workflow.newActivityStub(
            DespachoActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    .setScheduleToCloseTimeout(Duration.ofSeconds(20))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(3)
                            .setInitialInterval(Duration.ofMillis(100))
                            .setDoNotRetry("VALIDATION")
                            .build())
                    .build()
    );

    @Override
    public ReciboEnvio entregar(String ordenId, String unidad, String comandoId) {
        return activities.enviar(ordenId, unidad, comandoId);
    }
}
