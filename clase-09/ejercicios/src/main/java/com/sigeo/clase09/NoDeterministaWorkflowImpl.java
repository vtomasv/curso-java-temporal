package com.sigeo.clase09;

import io.temporal.workflow.Workflow;

public class NoDeterministaWorkflowImpl implements ProcesoDeterministaWorkflow {

    @Override
    public String ejecutarProceso() {
        String id = Workflow.randomUUID().toString();
        long inicio = Workflow.currentTimeMillis();
        Workflow.sleep(1000);
        double aleatorio = Workflow.newRandom().nextDouble();

        Workflow.getLogger(NoDeterministaWorkflowImpl.class).info(
                "Proceso ejecutado: id={}, inicio={}, aleatorio={}",
                id,
                inicio,
                aleatorio
        );

        return "Completado";
    }
}
