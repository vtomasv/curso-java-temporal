package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import java.time.Duration;

public class VencimientoWorkflowImpl implements VencimientoWorkflow {
    @Override
    public String procesarConVencimiento(int dias) {
        // TODO(C13-E01): Implementar la espera de 'dias' usando Workflow.sleep
        // y retornar "Vencido tras X días"
        throw new UnsupportedOperationException("TODO C13-E01");
    }
}
