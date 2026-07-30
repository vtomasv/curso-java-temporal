package com.sigeo.clase06;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

// TODO(C06-E04): Mapear entidad Aprobacion
public class Aprobacion {

    // TODO(C06-E04): Configurar ID como UUID
    private UUID id;

    private String responsable;
    private String comentario;
    private LocalDateTime fecha;

    // TODO(C06-E04): Mapear relación N:1 con Solicitud (dueño de la relación)
    private Solicitud solicitud;

    protected Aprobacion() {}

    public Aprobacion(String responsable, String comentario) {
        this.responsable = responsable;
        this.comentario = comentario;
        this.fecha = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getResponsable() { return responsable; }
    public String getComentario() { return comentario; }
    public LocalDateTime getFecha() { return fecha; }
    public Solicitud getSolicitud() { return solicitud; }
    public void setSolicitud(Solicitud solicitud) { this.solicitud = solicitud; }
}
