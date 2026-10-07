package com.sigeo.evaluacion02.workflow;

public record SolicitudWorkflowInput(
        String id,
        String descripcion,
        String claveIdempotencia
) {
}

