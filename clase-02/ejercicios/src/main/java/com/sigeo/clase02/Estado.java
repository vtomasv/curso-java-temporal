package com.sigeo.clase02;

public abstract class Estado {
    
    private String id;
    private String nombre;
    
    public abstract void aprobar(Solicitud solicitud, String firma);

    public abstract void rechazar(Solicitud solicitud, String firma);

    public abstract void enviarRevision(Solicitud solicitud, String firma);

    public abstract void enviarModificacion(Solicitud solicitud, String firma);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
