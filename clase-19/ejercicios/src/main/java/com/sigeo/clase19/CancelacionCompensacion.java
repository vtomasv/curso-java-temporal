package com.sigeo.clase19;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CancelacionCompensacion {

    @WorkflowMethod
    void ejecutarProceso(String id);
    
    // TODO(C19-E03): Implementar cancelación segura o nueva compensación idempotente
}
