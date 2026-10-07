package com.sigeo.evaluacion02.service;

public class SolicitudNoEncontradaException extends RuntimeException {

    public SolicitudNoEncontradaException(String id) {
        super("Solicitud no encontrada: " + id);
    }
}

