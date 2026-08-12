package com.sigeo.clase02;

public class Aprobado extends Estado {

    public Aprobado() {
        this.setNombre("APROBADA");
    }

    @Override
    public void aprobar(Solicitud solicitud, String firma) {
        System.out.println("Solicitud ya fue aprobada con firma: " + solicitud.getFirma());
    }

    @Override
    public void rechazar(Solicitud solicitud, String firma) {
        throw new IllegalStateException();
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
