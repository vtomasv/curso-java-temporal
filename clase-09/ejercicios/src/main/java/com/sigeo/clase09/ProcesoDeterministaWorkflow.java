package com.sigeo.clase09;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ProcesoDeterministaWorkflow {

    @WorkflowMethod
    String ejecutarProceso();
}
