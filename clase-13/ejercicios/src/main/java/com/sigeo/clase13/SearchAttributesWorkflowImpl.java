package com.sigeo.clase13;

import io.temporal.workflow.Workflow;
import java.util.HashMap;
import java.util.Map;

public class SearchAttributesWorkflowImpl implements SearchAttributesWorkflow {
    @Override
    public void ejecutarConAtributos(String responsable, String prioridad) {
        // TODO(C13-E05): Crear un Map con los atributos "Responsable", "Prioridad" y "Estado" (EN_PROGRESO)
        // Usar Workflow.upsertSearchAttributes para guardarlos
        // Hacer un Workflow.sleep de 1 segundo
        // Actualizar el "Estado" a "COMPLETADO" usando upsertSearchAttributes nuevamente
        throw new UnsupportedOperationException("TODO C13-E05");
    }
}
