package com.sigeo.clase09;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AprobacionActivity {

    @ActivityMethod
    void notificarResultado(String idSolicitud, String resultado);
}
