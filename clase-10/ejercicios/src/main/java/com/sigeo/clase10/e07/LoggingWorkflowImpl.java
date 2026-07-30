package com.sigeo.clase10.e07;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class LoggingWorkflowImpl implements LoggingWorkflow {

    private final LoggingActivity activity = Workflow.newActivityStub(LoggingActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    .build());

    @Override
    public void executeWork(String data) {
        activity.doWork(data);
    }
}
