package com.sigeo.clase09;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AprobacionWorkflow {

    @WorkflowMethod
    String solicitarAprobacion(String idSolicitud);

    @SignalMethod
    void recibirDecision(boolean aprobado);
}
