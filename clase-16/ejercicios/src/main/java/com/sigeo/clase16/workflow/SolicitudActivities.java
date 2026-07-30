package com.sigeo.clase16.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SolicitudActivities {

    @ActivityMethod
    void guardarEstadoSolicitud(Long solicitudId, String estado);
}
