package com.enlace.evaluacion03.web;

import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CrearOrdenRequest(
        @NotBlank String id,
        @NotBlank @Size(min = 5, max = 120) String asunto,
        @NotBlank @Size(min = 20, max = 2000) String contenido,
        @NotNull TipoOrden tipo,
        @NotNull Confidencialidad confidencialidad,
        @NotEmpty Set<UnidadDestino> destinatarios,
        @NotBlank String claveIdempotencia
) {
}
