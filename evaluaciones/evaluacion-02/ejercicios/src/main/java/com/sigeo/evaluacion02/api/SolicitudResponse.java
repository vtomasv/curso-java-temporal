package com.sigeo.evaluacion02.api;

import com.sigeo.evaluacion02.domain.EstadoSolicitud;
import com.sigeo.evaluacion02.domain.Prioridad;
import com.sigeo.evaluacion02.domain.Solicitud;

public record SolicitudResponse(
        String id,
        String solicitante,
        String descripcion,
        Prioridad prioridad,
        EstadoSolicitud estado,
        String propietario,
        long version
) {
    public static SolicitudResponse from(Solicitud solicitud) {
        // TODO(EV02-E03): Mapear todos los campos sin exponer la entidad JPA.
        return null;
    }
}
