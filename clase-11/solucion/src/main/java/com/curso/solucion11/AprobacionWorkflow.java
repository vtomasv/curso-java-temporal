package com.curso.solucion11;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.SignalMethod;

@WorkflowInterface
public interface AprobacionWorkflow {
    @WorkflowMethod
    void iniciar(String id);
    @SignalMethod
    void aprobar();
}
