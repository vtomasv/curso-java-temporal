package com.sigeo.evaluacion02.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

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
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("El id no puede ser nulo o vacío");

        this.id = id;
        if (solicitante == null || solicitante.trim().isEmpty()) throw new IllegalArgumentException("El solicitante no puede ser nulo o vacío");
        if (descripcion == null || descripcion.trim().isEmpty()) throw new IllegalArgumentException("La descripcion no puede ser nula o vacía");
        if (prioridad == null) throw new IllegalArgumentException("La prioridad no puede ser nula");
        if (propietario == null || propietario.trim().isEmpty()) throw new IllegalArgumentException("El propietario no puede ser nulo o vacío");
        if (claveIdempotencia == null || claveIdempotencia.trim().isEmpty()) throw new IllegalArgumentException("La clave de idempotencia no puede ser nula");
        this.solicitante = solicitante;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.propietario = propietario;
        this.claveIdempotencia = claveIdempotencia;
        this.estado =   EstadoSolicitud.PENDIENTE;
    }


    public void aprobar() {
        if(this.estado != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud " + this.id + " no se encuentra en estado PENDIENTE.");
        }else { 
            this.estado = EstadoSolicitud.APROBADA;
        }

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

