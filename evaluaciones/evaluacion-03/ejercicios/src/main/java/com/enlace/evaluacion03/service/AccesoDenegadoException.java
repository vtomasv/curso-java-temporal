package com.enlace.evaluacion03.service;

public class AccesoDenegadoException extends RuntimeException {
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
