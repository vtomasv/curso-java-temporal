package com.sigeo.clase13;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.List;

@WorkflowInterface
public interface InteraccionWorkflow {
    @WorkflowMethod
    List<String> ejecutar();

    @SignalMethod
    void agregarEvento(String evento);

    @UpdateMethod
    String actualizarEstado(String nuevoEstado);

    @SignalMethod
    void completar();
    
    @QueryMethod
    List<String> consultarEventos();
}
