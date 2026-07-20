package com.curso.solucion10;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface NotificacionActivity {
    @ActivityMethod
    void enviarNotificacion(String mensaje);
}
