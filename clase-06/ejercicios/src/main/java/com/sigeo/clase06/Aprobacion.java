package com.sigeo.clase06;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "aprobacion")
public class Aprobacion {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String responsable;
    private String comentario;
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
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
