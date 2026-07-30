package com.sigeo.clase10.e02;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class ServiceWorkflowImpl implements ServiceWorkflow {

    // TODO(C10-E02): Configurar RetryOptions para reintentar errores 503 con backoff.
    // El error "400" debe ser clasificado como no reintentable (setDoNotRetry).
    // Configura un máximo de 5 intentos.
    private final ServiceActivity activity = Workflow.newActivityStub(ServiceActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    // .setRetryOptions(...)
                    .build());

    @Override
    public String executeService(String input) {
        return activity.processRequest(input);
    }
}
