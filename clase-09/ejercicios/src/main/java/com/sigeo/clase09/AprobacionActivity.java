package com.sigeo.clase09;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AprobacionActivity {

    // TODO(C09-E06): Definir método para notificar el resultado
    // El método debe llamarse 'notificarResultado' y recibir String 'idSolicitud' y String 'resultado'.
    void notificarResultado(String idSolicitud, String resultado);
}
