package com.sigeo.clase14;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class AnalisisWorkflowImpl implements AnalisisWorkflow {

    // TODO(C14-E06): Configurar ActivityOptions con timeout y retry limitado
    private final AnalisisAiActivity activity = Workflow.newActivityStub(AnalisisAiActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(30))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(3)
                            .build())
                    .build());

    @Override
    public String ejecutarAnalisis(String texto) {
        // TODO(C14-E06): Llamar a la activity y devolver el resultado
        throw new UnsupportedOperationException("TODO C14-E06");
    }
}
