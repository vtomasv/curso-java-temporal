package com.sigeo.clase02;

/**
 * Rechazado
 */
public class Rechazado extends Estado {

    public Rechazado() {
        this.setNombre("RECHAZADA");
    }

    @Override
    public void aprobar(Solicitud solicitud, String firma) {
        throw new IllegalStateException();
    }

    @Override
    public void rechazar(Solicitud solicitud, String firma) {
        System.out.println("Solicitud ya se encuentra rechazada con firma: " + solicitud.getFirma());
    }

    @Override
    public void enviarRevision(Solicitud solicitud, String firma) {
        throw new IllegalStateException();
    }

    @Override
    public void enviarModificacion(Solicitud solicitud, String firma) {
        throw new IllegalStateException();
    }

}
