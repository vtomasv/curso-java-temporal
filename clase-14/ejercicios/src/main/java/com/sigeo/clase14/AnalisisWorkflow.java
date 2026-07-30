package com.sigeo.clase14;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AnalisisWorkflow {

    /**
     * E06: Análisis durable (Workflow)
     * Orquestar la llamada a la Activity.
     */
    @WorkflowMethod
    String ejecutarAnalisis(String texto);
}
