package com.sigeo.clase10.e07;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface LoggingWorkflow {
    @WorkflowMethod
    void executeWork(String data);
}
