package com.sigeo.clase09;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class AprobacionWorkflowImpl implements AprobacionWorkflow {

    private final AprobacionActivity activity = Workflow.newActivityStub(
            AprobacionActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );
    
    private Boolean decision = null;

    @Override
    public String solicitarAprobacion(String idSolicitud) {
        Workflow.await(Duration.ofDays(7), () -> decision != null);

        String resultado = decision == null
                ? "VENCIDA"
                : decision ? "APROBADA" : "RECHAZADA";

        activity.notificarResultado(idSolicitud, resultado);
        return resultado;
    }

    @Override
    public void recibirDecision(boolean aprobado) {
        this.decision = aprobado;
    }
}
