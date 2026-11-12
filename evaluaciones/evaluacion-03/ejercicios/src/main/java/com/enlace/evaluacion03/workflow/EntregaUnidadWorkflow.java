package com.enlace.evaluacion03.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface EntregaUnidadWorkflow {

    @WorkflowMethod
    ReciboEnvio entregar(String ordenId, String unidad, String comandoId);
}
