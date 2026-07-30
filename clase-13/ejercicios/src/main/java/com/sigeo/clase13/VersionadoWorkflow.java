package com.sigeo.clase13;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface VersionadoWorkflow {
    @WorkflowMethod
    String procesar();
}
