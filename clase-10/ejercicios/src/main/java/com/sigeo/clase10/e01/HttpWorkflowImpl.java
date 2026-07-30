package com.sigeo.clase10.e01;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class HttpWorkflowImpl implements HttpWorkflow {

    // TODO(C10-E01): Configurar ActivityOptions con un StartToCloseTimeout de 2 segundos.
    // Asegúrate de que no haya reintentos infinitos (configura un RetryOptions con maxAttempts = 1 para este ejercicio).
    private final HttpActivity activity = Workflow.newActivityStub(HttpActivity.class,
            ActivityOptions.newBuilder()
                    // .setStartToCloseTimeout(...)
                    // .setRetryOptions(...)
                    .build());

    @Override
    public String executeCall(int latencySeconds) {
        return activity.callExternalService(latencySeconds);
    }
}
