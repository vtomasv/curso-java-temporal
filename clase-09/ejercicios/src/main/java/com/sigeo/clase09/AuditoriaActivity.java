package com.sigeo.clase09;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AuditoriaActivity {

    @ActivityMethod
    void registrarAuditoria(String mensaje);
}
