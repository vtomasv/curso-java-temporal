package com.sigeo.clase06;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO(C06-E01): Mapear entidad Solicitud con @Entity, @Table, @Id (UUID), @Version, y timestamps
public class Solicitud {

    // TODO(C06-E01): Configurar ID como UUID generado automáticamente
    private UUID id;

    private String descripcion;
    private String estado;
    private Integer prioridad;

    // TODO(C06-E02): Mapear Contacto como @Embedded
    private Contacto contacto;

    // TODO(C06-E04): Mapear relación 1:N con Aprobacion (dueño de relación: Aprobacion), usar cascade y orphanRemoval
    private List<Aprobacion> aprobaciones = new ArrayList<>();

    // TODO(C06-E01): Mapear timestamps
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // TODO(C06-E01): Mapear @Version para optimistic locking
    private Long version;

    // TODO(C06-E08): Agregar nuevo campo obligatorio 'departamento' (String)
    // private String departamento;

    protected Solicitud() {
        // JPA requiere constructor sin argumentos
    }

    public Solicitud(String descripcion, String estado, Integer prioridad) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getPrioridad() { return prioridad; }
    public void setPrioridad(Integer prioridad) { this.prioridad = prioridad; }
    public Contacto getContacto() { return contacto; }
    public void setContacto(Contacto contacto) { this.contacto = contacto; }
    public List<Aprobacion> getAprobaciones() { return aprobaciones; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public Long getVersion() { return version; }

    // TODO(C06-E04): Implementar método utilitario para agregar aprobación (sincronizar ambos lados de la relación)
    public void addAprobacion(Aprobacion aprobacion) {
        throw new UnsupportedOperationException("TODO C06-E04");
    }
}
