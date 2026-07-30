package com.sigeo.clase09;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AprobacionWorkflow {

    // TODO(C09-E06): Definir el método principal del workflow
    // El método debe llamarse 'solicitarAprobacion' y recibir un String 'idSolicitud', devolviendo un String con el estado final.
    String solicitarAprobacion(String idSolicitud);

    // TODO(C09-E06): Definir un método de señal para recibir la decisión
    // El método debe llamarse 'recibirDecision' y recibir un boolean 'aprobado'.
    @SignalMethod
    void recibirDecision(boolean aprobado);
}
