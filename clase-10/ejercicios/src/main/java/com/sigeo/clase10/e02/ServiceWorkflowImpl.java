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
