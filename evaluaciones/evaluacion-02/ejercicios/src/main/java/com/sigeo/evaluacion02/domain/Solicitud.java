package com.sigeo.evaluacion02.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @Column(nullable = false, updatable = false, length = 40)
    private String id;

    @Column(nullable = false, length = 100)
    private String solicitante;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSolicitud estado;

    @Column(nullable = false, length = 80)
    private String propietario;

    @Column(nullable = false, unique = true, updatable = false, length = 80)
    private String claveIdempotencia;

    @Version
    private long version;

    protected Solicitud() {
        // Requerido por JPA.
    }

    public Solicitud(
            String id,
            String solicitante,
            String descripcion,
            Prioridad prioridad,
            String propietario,
            String claveIdempotencia
    ) {
        // TODO(EV02-E01): Validar textos nulos/en blanco y prioridad nula.
        // El estado inicial debe ser PENDIENTE.
        this.id = id;
        this.solicitante = solicitante;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.propietario = propietario;
        this.claveIdempotencia = claveIdempotencia;
        this.estado = null;
    }

    public void aprobar() {
        // TODO(EV02-E02): Permitir PENDIENTE -> APROBADA una sola vez.
        // Una segunda aprobación debe lanzar IllegalStateException con contexto.
    }

    public String getId() {
        return id;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public String getPropietario() {
        return propietario;
    }

    public String getClaveIdempotencia() {
        return claveIdempotencia;
    }

    public long getVersion() {
        return version;
    }
}

