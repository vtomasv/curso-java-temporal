package com.sigeo.clase12;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ResourceChildWorkflow {

    @WorkflowMethod
    void processResource(String reservationId);
}
