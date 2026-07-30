package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import java.util.ArrayList;
import java.util.List;

public class InteraccionWorkflowImpl implements InteraccionWorkflow {
    private final List<String> eventos = new ArrayList<>();
    private boolean completado = false;

    @Override
    public List<String> ejecutar() {
        // TODO(C13-E02): Esperar hasta que completado sea true usando Workflow.await
        // y retornar la lista de eventos
        throw new UnsupportedOperationException("TODO C13-E02");
    }

    @Override
    public void agregarEvento(String evento) {
        // TODO(C13-E02): Agregar el evento a la lista
        throw new UnsupportedOperationException("TODO C13-E02");
    }

    @Override
    public String actualizarEstado(String nuevoEstado) {
        // TODO(C13-E02): Validar que nuevoEstado no sea nulo ni vacío (lanzar IllegalArgumentException)
        // Agregar "Estado actualizado a: " + nuevoEstado a la lista de eventos
        // Retornar "OK"
        throw new UnsupportedOperationException("TODO C13-E02");
    }

    @Override
    public void completar() {
        // TODO(C13-E02): Marcar completado como true
        throw new UnsupportedOperationException("TODO C13-E02");
    }
    
    @Override
    public List<String> consultarEventos() {
        // TODO(C13-E02): Retornar la lista de eventos
        throw new UnsupportedOperationException("TODO C13-E02");
    }
}
