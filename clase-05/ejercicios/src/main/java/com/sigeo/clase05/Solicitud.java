package com.sigeo.clase05;

public class Solicitud {
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;

    public Solicitud(Long id, String titulo, String descripcion, String estado, String prioridad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getPrioridad() { return prioridad; }
}
