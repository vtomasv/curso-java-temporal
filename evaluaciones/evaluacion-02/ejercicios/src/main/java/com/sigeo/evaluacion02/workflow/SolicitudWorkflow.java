package com.sigeo.evaluacion02.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SolicitudWorkflow {

    @WorkflowMethod
    SolicitudWorkflowResult procesar(SolicitudWorkflowInput input);
}

