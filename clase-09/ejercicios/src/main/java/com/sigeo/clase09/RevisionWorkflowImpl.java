package com.sigeo.clase09;

import io.temporal.workflow.Workflow;
import java.time.Duration;

public class RevisionWorkflowImpl implements RevisionWorkflow {

    @Override
    public String iniciarRevision(int diasEspera) {
        // TODO(C09-E04): Usar Workflow.sleep para simular la espera
        // NO usar Thread.sleep. Usar Duration.ofDays(diasEspera)
        
        // Retornar "Revisión completada después de " + diasEspera + " días"
        throw new UnsupportedOperationException("TODO C09-E04");
    }
}
