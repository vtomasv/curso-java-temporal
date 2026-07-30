package com.sigeo.clase11;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface LongRunningWorkflow {

    @WorkflowMethod
    void processEvents(int processedCount);

    @SignalMethod
    void addEvent(String event);
}
