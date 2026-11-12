package com.enlace.evaluacion03.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface DespachoWorkflow {

    @WorkflowMethod
    ResultadoDespacho iniciar(DespachoInput input);

    @SignalMethod
    void confirmarRecepcion(String unidad, String reciboId);

    @QueryMethod
    ProgresoDespacho consultarProgreso();

    @UpdateMethod
    boolean cancelar(String motivo);
}
