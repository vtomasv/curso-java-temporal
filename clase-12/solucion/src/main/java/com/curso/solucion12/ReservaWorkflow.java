package com.curso.solucion12;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ReservaWorkflow {
    @WorkflowMethod
    void reservar();
}
