package com.sigeo.clase13;

import io.temporal.workflow.Workflow;

public class ReplayWorkflowImpl implements ReplayWorkflow {
    @Override
    public String procesar() {
        // TODO(C13-E03): Este código es diferente al que generó la historia.
        // La historia original hacía:
        // 1. Workflow.sleep(Duration.ofSeconds(1))
        // 2. return "Procesado"
        // 
        // Modifica este código para que cause un error de no-determinismo al hacer replay,
        // por ejemplo, cambiando el orden o agregando un sleep extra.
        throw new UnsupportedOperationException("TODO C13-E03");
    }
}
