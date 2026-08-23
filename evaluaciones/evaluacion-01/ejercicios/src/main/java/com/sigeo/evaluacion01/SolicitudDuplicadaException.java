package com.sigeo.evaluacion01;

/** Error de dominio para el intento de registrar un identificador existente. */
public class SolicitudDuplicadaException extends RuntimeException {

    public SolicitudDuplicadaException(String id) {
        super("Solicitud duplicada: " + id);
    }
}

