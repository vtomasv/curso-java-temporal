package com.enlace.evaluacion03.service;

public class OrdenNoEncontradaException extends RuntimeException {
    public OrdenNoEncontradaException(String id) {
        super("Orden no encontrada: " + id);
    }
}
