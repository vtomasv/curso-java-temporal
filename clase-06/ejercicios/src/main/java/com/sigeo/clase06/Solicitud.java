package com.sigeo.clase06;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "solicitud")
public class Solicitud {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String descripcion;
    private String estado;
    private Integer prioridad;

    @Embedded
    private Contacto contacto;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aprobacion> aprobaciones = new ArrayList<>();

    @org.hibernate.annotations.CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime fechaCreacion;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @Version
    private Long version;

    @Column(nullable = false)
    private String departamento;

    protected Solicitud() {
        // JPA requiere constructor sin argumentos
    }

    public Solicitud(String descripcion, String estado, Integer prioridad) {
        this(descripcion, estado, prioridad, "General");
    }

    public Solicitud(String descripcion, String estado, Integer prioridad, String departamento) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.departamento = departamento;
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

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    // TODO(C06-E04): Implementar método utilitario para agregar aprobación (sincronizar ambos lados de la relación)
    public void addAprobacion(Aprobacion aprobacion) {
        if (aprobacion == null) return;
        this.aprobaciones.add(aprobacion);
        aprobacion.setSolicitud(this);
    }
}
