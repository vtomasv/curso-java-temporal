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
