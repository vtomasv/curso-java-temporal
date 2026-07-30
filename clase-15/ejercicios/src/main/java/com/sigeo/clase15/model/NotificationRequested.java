package com.sigeo.clase15.model;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequested(
    // TODO(C15-E03): Agregar anotaciones de validación (@NotBlank) a los campos
    String correlationId,
    String recipient,
    String message
) {}
