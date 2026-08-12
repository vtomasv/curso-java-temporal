package com.sigeo.clase02;

public class Solicitud {

    private final String id;
    private String firma;
    private Estado estado;
    private String motivo;

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Solicitud(String motivo) {
        this.id = java.util.UUID.randomUUID().toString();
        this.motivo = motivo;
        this.estado = new EnRevision();
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }


    public void aprobar(String firma) {
        estado.aprobar(this, firma);
    }

    public void rechazar(String firma) {
        estado.rechazar(this, firma);
    }

    public void enviarRevision(String firma) {
        estado.enviarRevision(this, firma);
    }

    public void enviarModificacion(String firma) {
        estado.enviarModificacion(this, firma);
    }

    public String getId() {
        return id;
    }

}
