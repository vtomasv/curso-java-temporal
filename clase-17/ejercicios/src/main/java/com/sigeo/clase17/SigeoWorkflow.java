package com.sigeo.clase17;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SigeoWorkflow {

    @WorkflowMethod
    void processRequest(String requestId);
}
