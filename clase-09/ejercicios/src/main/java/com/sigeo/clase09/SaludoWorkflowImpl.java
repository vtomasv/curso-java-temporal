package com.sigeo.clase09;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SaludoWorkflowImpl implements SaludoWorkflow {

    private final AuditoriaActivity auditoriaActivity = Workflow.newActivityStub(
            AuditoriaActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public String saludar(String nombre) {
        auditoriaActivity.registrarAuditoria("Se saludó a: " + nombre);
        return "Hola, " + nombre;
    }
}
