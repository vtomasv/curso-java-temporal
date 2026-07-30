package com.sigeo.clase10.e05;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class BatchProcessingWorkflowImpl implements BatchProcessingWorkflow {

    private final BatchProcessingActivity activity = Workflow.newActivityStub(BatchProcessingActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(1))
                    .setHeartbeatTimeout(Duration.ofSeconds(2))
                    .build());

    @Override
    public int executeBatch(int totalRecords) {
        return activity.processBatch(totalRecords);
    }
}
