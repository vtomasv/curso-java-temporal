package com.sigeo.clase16.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SolicitudWorkflowImpl implements SolicitudWorkflow {

    private String estado = "INICIADO";

    private final SolicitudActivities activities = Workflow.newActivityStub(
            SolicitudActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public void procesarSolicitud(Long solicitudId) {
        // TODO(C16-E08): Implementar la lógica del workflow
        // 1. Actualizar estado a "PROCESANDO"
        // 2. Llamar a la actividad guardarEstadoSolicitud
        // 3. Actualizar estado a "COMPLETADO"
        // 4. Llamar a la actividad guardarEstadoSolicitud
        throw new UnsupportedOperationException("TODO C16-E08: Implementar procesarSolicitud");
    }

    @Override
    public String getEstadoActual() {
        return estado;
    }
}
