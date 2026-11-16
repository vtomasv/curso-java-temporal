package com.enlace.evaluacion03.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.UpdateValidatorMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

/**
 * Auditoría durable de lecturas autorizadas para una orden. Se mantiene
 * independiente del despacho para continuar aceptando accesos cuando el
 * Workflow de entrega ya alcanzó un estado terminal.
 */
@WorkflowInterface
public interface AccesoWorkflow {

    @WorkflowMethod
    void mantener(String ordenId);

    /**
     * Registra evidencia no sensible después de que la capa web autorizó la
     * lectura. La contraseña y el cuerpo de la orden nunca son argumentos.
     */
    @UpdateMethod(name = "registrarAcceso")
    AccesoProgreso registrarAcceso(String accesoId, String usuario, boolean reautenticado);

    /** Rechaza un identificador inválido o reutilizado con otra evidencia. */
    @UpdateValidatorMethod(updateName = "registrarAcceso")
    void validarRegistrarAcceso(String accesoId, String usuario, boolean reautenticado);

    @QueryMethod
    AccesoProgreso consultarAccesos();
}
