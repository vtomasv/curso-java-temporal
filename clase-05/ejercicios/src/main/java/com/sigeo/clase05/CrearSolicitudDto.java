package com.sigeo.clase05;

import jakarta.validation.constraints.NotBlank;

// TODO(C05-E03): Convertir a record y agregar validaciones (@NotBlank)

public class CrearSolicitudDto {
    private String titulo;
    private String descripcion;
    private String prioridad;

    public String getTitulo() { return titulo; }

    @NotBlank(message = "El título no puede estar vacío")
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    
    @NotBlank(message = "La descripción no puede estar vacía")
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    
    public String getPrioridad() { return prioridad; }

    @NotBlank(message = "La prioridad no puede estar vacía")
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
}
