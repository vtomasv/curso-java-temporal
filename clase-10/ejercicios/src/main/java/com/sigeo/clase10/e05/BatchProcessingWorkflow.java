package com.sigeo.clase10.e05;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface BatchProcessingWorkflow {
    @WorkflowMethod
    int executeBatch(int totalRecords);
}
