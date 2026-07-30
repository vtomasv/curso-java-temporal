package com.sigeo.clase09;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AuditoriaActivity {

    // TODO(C09-E03): Definir el método de la activity con @ActivityMethod
    // El método debe llamarse 'registrarAuditoria' y recibir un String 'mensaje'.
    void registrarAuditoria(String mensaje);
}
