package com.sigeo.evaluacion02.api;

import com.sigeo.evaluacion02.domain.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearSolicitudRequest(
        @NotBlank @Size(max = 40) String id,
        @NotBlank @Size(max = 100) String solicitante,
        @NotBlank @Size(max = 500) String descripcion,
        @NotNull Prioridad prioridad
) {
}

