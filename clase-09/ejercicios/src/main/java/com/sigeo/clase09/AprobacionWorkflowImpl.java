package com.sigeo.clase09;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class AprobacionWorkflowImpl implements AprobacionWorkflow {

    private final AprobacionActivity activity = null; // TODO(C09-E06): Inicializar stub
    
    private Boolean decision = null;

    @Override
    public String solicitarAprobacion(String idSolicitud) {
        // TODO(C09-E06): Implementar la lógica de aprobación
        // 1. Esperar hasta 7 días por una decisión usando Workflow.await
        // 2. Si decision es null después de 7 días, el resultado es "VENCIDA"
        // 3. Si decision es true, el resultado es "APROBADA"
        // 4. Si decision es false, el resultado es "RECHAZADA"
        // 5. Llamar a activity.notificarResultado(idSolicitud, resultado)
        // 6. Retornar el resultado
        
        throw new UnsupportedOperationException("TODO C09-E06");
    }

    @Override
    public void recibirDecision(boolean aprobado) {
        // TODO(C09-E06): Guardar la decisión en la variable de estado
        throw new UnsupportedOperationException("TODO C09-E06");
    }
}
