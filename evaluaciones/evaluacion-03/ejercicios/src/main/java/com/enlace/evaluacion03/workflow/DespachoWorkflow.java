package com.enlace.evaluacion03.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.UpdateValidatorMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.util.List;

@WorkflowInterface
public interface DespachoWorkflow {

    @WorkflowMethod
    ResultadoDespacho iniciar(DespachoInput input);

    /**
     * Registra de forma durable una firma ya autenticada por la interfaz. La
     * respuesta se entrega sólo después de persistirla mediante una Activity.
     */
    @UpdateMethod(name = "registrarFirma")
    FirmaProgreso registrarFirma(String firmante, String rol);

    /**
     * Rechaza contratos inválidos antes de aceptar el Update. Una excepción en
     * este validador falla sólo la solicitud y no el Workflow Task.
     */
    @UpdateValidatorMethod(updateName = "registrarFirma")
    void validarRegistrarFirma(String firmante, String rol);

    @SignalMethod
    void confirmarRecepcion(String unidad, String reciboId);

    @QueryMethod
    ProgresoDespacho consultarProgreso();

    @QueryMethod
    List<ReciboEnvio> consultarRecibos();

    @QueryMethod
    FirmaProgreso consultarFirmas();

    @UpdateMethod
    boolean cancelar(String motivo);
}
