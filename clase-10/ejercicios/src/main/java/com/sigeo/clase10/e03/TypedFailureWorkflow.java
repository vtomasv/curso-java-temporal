package com.sigeo.clase10.e03;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TypedFailureWorkflow {
    @WorkflowMethod
    String process(String data);
}
