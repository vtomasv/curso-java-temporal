package com.sigeo.clase13;

import io.temporal.workflow.Workflow;

public class VersionadoWorkflowImpl implements VersionadoWorkflow {
    @Override
    public String procesar() {
        // TODO(C13-E04): Usar Workflow.getVersion("ValidacionExtra", Workflow.DEFAULT_VERSION, 1)
        // Si es DEFAULT_VERSION, retornar "Procesado sin validación extra"
        // Si es 1, retornar "Procesado CON validación extra"
        throw new UnsupportedOperationException("TODO C13-E04");
    }
}
