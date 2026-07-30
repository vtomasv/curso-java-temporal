package com.sigeo.clase10.e01;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface HttpWorkflow {
    @WorkflowMethod
    String executeCall(int latencySeconds);
}
