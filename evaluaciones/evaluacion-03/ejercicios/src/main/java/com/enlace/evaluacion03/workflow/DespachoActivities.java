package com.enlace.evaluacion03.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DespachoActivities {

    @ActivityMethod
    ReciboEnvio enviar(String ordenId, String unidad, String comandoId);

    @ActivityMethod
    void registrarEstado(String ordenId, EstadoDespacho estado);

    @ActivityMethod
    void compensar(String ordenId, String unidad, String reciboId);
}
