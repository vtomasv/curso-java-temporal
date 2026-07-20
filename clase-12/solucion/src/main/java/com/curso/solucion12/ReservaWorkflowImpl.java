package com.curso.solucion12;

import io.temporal.workflow.Saga;
import io.temporal.spring.boot.WorkflowImpl;

@WorkflowImpl(taskQueues="ViajesTaskQueue")
public class ReservaWorkflowImpl implements ReservaWorkflow {
    public void reservar() {
        Saga saga = new Saga(new Saga.Options.Builder().build());
        saga.addCompensation(() -> System.out.println("Rollback"));
    }
}
