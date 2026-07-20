package com.curso.solucion17;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface LogisticaWorkflow {
    @WorkflowMethod
    void despachar(String ordenId);
}
