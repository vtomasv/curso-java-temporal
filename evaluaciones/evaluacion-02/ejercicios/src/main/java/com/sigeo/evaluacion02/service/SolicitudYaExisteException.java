package com.sigeo.evaluacion02.service;

import com.sigeo.evaluacion02.domain.Solicitud;

/**
 * SolicitudYaExisteException
 */
public class SolicitudYaExisteException extends RuntimeException {

    private final Solicitud solicitud;

    public SolicitudYaExisteException(Solicitud solicitud) {
        super("Solicitud ya existe: " + solicitud.getId());
        this.solicitud = solicitud;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }
}
