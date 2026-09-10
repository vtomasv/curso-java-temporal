package com.sigeo.clase05;

// TODO(C05-E03): Convertir a record
public record SolicitudResponseDto(Long id, String titulo, String descripcion, String estado, String prioridad) {
    
    public SolicitudResponseDto(Long id, String titulo, String descripcion, String estado, String prioridad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
    }
    
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public String getPrioridad() { return prioridad; }
}
