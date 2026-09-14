package com.sigeo.clase05;

import jakarta.validation.constraints.NotBlank;

public record CrearSolicitudDto(
    @NotBlank(message = "El título no puede estar vacío") String titulo,
    @NotBlank(message = "La descripción no puede estar vacía") String descripcion,
    @NotBlank(message = "La prioridad no puede estar vacía") String prioridad
) {}
