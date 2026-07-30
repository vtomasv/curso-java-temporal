package com.sigeo.clase10.e06;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class ExportWorkflowImpl implements ExportWorkflow {

    private final ExportActivity activity = Workflow.newActivityStub(ExportActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(5))
                    .setHeartbeatTimeout(Duration.ofSeconds(2))
                    .setCancellationType(io.temporal.activity.ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
                    .build());

    @Override
    public void startExport() {
        activity.exportData();
    }
}
