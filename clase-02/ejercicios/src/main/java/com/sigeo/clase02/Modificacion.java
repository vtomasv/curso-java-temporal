package com.sigeo.clase02;

/**
 * Modificacion
 */
public class Modificacion extends Estado {

    public Modificacion() {
        this.setNombre("EN MODIFICACION");
    }

    @Override
    public void aprobar(Solicitud solicitud, String firma) {
        solicitud.setFirma(firma);
        solicitud.setEstado(new Aprobado());
    }

    @Override
    public void rechazar(Solicitud solicitud, String firma) {
        solicitud.setFirma(firma);
        solicitud.setEstado(new Rechazado());
    }

    @Override
    public void enviarRevision(Solicitud solicitud, String firma) {
        solicitud.setFirma(firma);
        solicitud.setEstado(new EnRevision());
    }

    @Override
    public void enviarModificacion(Solicitud solicitud, String firma) {
        System.out.println("Solicitud ya se encuentra en modificación con firma: " + solicitud.getFirma());
    }

}
