package com.sigeo.clase15.model;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequestedV2(
    @NotBlank String correlationId,
    @NotBlank String recipient,
    @NotBlank String message,
    // TODO(C15-E08): Agregar campo 'priority' compatible hacia atrás
    String priority
) {
    public String getPriorityOrDefault() {
        return priority != null ? priority : "NORMAL";
    }
}
