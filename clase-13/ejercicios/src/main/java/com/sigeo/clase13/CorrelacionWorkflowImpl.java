package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class CorrelacionWorkflowImpl implements CorrelacionWorkflow {
    // TODO(C13-E06): Obtener el logger seguro para replay usando Workflow.getLogger(CorrelacionWorkflowImpl.class)
    
    @Override
    public void procesar(String correlationId) {
        // TODO(C13-E06): Loguear "Iniciando procesamiento con correlationId: {}"
        // Llamar a la actividad (simulada aquí con un sleep)
        // Loguear "Finalizando procesamiento con correlationId: {}"
        throw new UnsupportedOperationException("TODO C13-E06");
    }
}
