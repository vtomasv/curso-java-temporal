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
        SolicitudResponse response = new SolicitudResponse(
                solicitud.getId(),
                solicitud.getSolicitante(),
                solicitud.getDescripcion(),
                solicitud.getPrioridad(),
                solicitud.getEstado(),
                solicitud.getPropietario(),
                solicitud.getVersion()
        );
        return response;
    }
}
