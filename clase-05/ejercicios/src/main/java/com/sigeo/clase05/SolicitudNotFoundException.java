package com.sigeo.clase05;

public class SolicitudNotFoundException extends RuntimeException {
    public SolicitudNotFoundException(Long id) {
        super("Solicitud no encontrada con ID: " + id);
    }
}
