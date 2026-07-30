package com.sigeo.clase09;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SaludoWorkflowImpl implements SaludoWorkflow {

    // TODO(C09-E03): Configurar el stub de la activity AuditoriaActivity
    // Usar Workflow.newActivityStub con ActivityOptions (ej. startToCloseTimeout de 10 segundos)
    private final AuditoriaActivity auditoriaActivity = null;

    @Override
    public String saludar(String nombre) {
        // TODO(C09-E02): Implementar la lógica del saludo
        // Debe retornar "Hola, " + nombre
        
        // TODO(C09-E03): Llamar a la activity para registrar la auditoría
        // El mensaje debe ser "Se saludó a: " + nombre
        
        throw new UnsupportedOperationException("TODO C09-E02 y C09-E03");
    }
}
