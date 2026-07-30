package com.sigeo.clase10.e02;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ServiceWorkflow {
    @WorkflowMethod
    String executeService(String input);
}
