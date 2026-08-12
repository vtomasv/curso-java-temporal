package com.sigeo.clase02;

/**
 * EnRevision
 */
public class EnRevision extends Estado {

    public EnRevision() {
        this.setNombre("EN REVISION");
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
        System.out.println("Solicitud ya se encuentra en revisión con firma: " + solicitud.getFirma());
    }

    @Override
    public void enviarModificacion(Solicitud solicitud, String firma) {
        solicitud.setFirma(firma);
        solicitud.setEstado(new Modificacion());
    }

}
