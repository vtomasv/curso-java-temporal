package com.sigeo.evaluacion02.workflow;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface SolicitudActivities {

    String registrar(SolicitudWorkflowInput input);

    void notificar(String solicitudId);
}

