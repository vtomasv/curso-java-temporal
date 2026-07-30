package com.sigeo.clase16.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.QueryMethod;

@WorkflowInterface
public interface SolicitudWorkflow {

    @WorkflowMethod
    void procesarSolicitud(Long solicitudId);

    @QueryMethod
    String getEstadoActual();
}
