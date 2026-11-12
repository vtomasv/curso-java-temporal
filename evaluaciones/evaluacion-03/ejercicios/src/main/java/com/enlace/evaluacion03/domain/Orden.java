package com.enlace.evaluacion03.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    private String id;

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false, length = 4000)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOrden tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Confidencialidad confidencialidad;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UnidadDestino> destinatarios = new LinkedHashSet<>();

    @Column(nullable = false)
    private String creador;

    @Column(nullable = false, unique = true)
    private String claveIdempotencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(nullable = false)
    private Instant creadaEn;

    @Version
    private long version;

    protected Orden() {
    }

    public Orden(String id, String asunto, String contenido, TipoOrden tipo,
                 Confidencialidad confidencialidad, Collection<UnidadDestino> destinatarios,
                 String creador, String claveIdempotencia) {
        this.id = texto(id, "id");
        this.asunto = texto(asunto, "asunto");
        this.contenido = texto(contenido, "contenido");
        this.tipo = requerido(tipo, "tipo");
        this.confidencialidad = requerido(confidencialidad, "confidencialidad");
        Collection<UnidadDestino> destinosRecibidos = requerido(destinatarios, "destinatarios");
        if (destinosRecibidos.isEmpty()) {
            throw new IllegalArgumentException("destinatarios");
        }
        this.destinatarios = new LinkedHashSet<>(destinosRecibidos);
        if (this.destinatarios.size() != destinosRecibidos.size()) {
            throw new IllegalArgumentException("destinatarios duplicados");
        }
        this.creador = texto(creador, "creador");
        this.claveIdempotencia = texto(claveIdempotencia, "claveIdempotencia");
        this.estado = EstadoOrden.PENDIENTE_FIRMAS;
        this.creadaEn = Instant.now();

        // TODO(EV03-E01): Elija dónde vivirán las invariantes avanzadas y justifique
        // la decisión. Debe preservar la validación de destinatarios y completar el
        // rango de contenido publicado, sin acoplar la entidad a Spring ni a la vista.
    }

    public void marcarListaParaEnvio() {
        if (estado != EstadoOrden.PENDIENTE_FIRMAS) {
            throw new IllegalStateException("La orden " + id + " no admite más firmas");
        }
        estado = EstadoOrden.LISTA_PARA_ENVIO;
    }

    public void marcarEnDespacho() {
        if (estado != EstadoOrden.LISTA_PARA_ENVIO) {
            throw new IllegalStateException("La orden " + id + " aún no está autorizada");
        }
        estado = EstadoOrden.EN_DESPACHO;
    }

    public void marcarResultado(EstadoOrden nuevoEstado) {
        if (estado != EstadoOrden.EN_DESPACHO) {
            throw new IllegalStateException("La orden " + id + " no está en despacho");
        }
        if (nuevoEstado != EstadoOrden.ENTREGADA
                && nuevoEstado != EstadoOrden.PENDIENTE_REVISION
                && nuevoEstado != EstadoOrden.CANCELADA) {
            throw new IllegalArgumentException("nuevoEstado");
        }
        estado = nuevoEstado;
    }

    private static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo);
        }
        return valor.trim();
    }

    private static <T> T requerido(T valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException(campo);
        }
        return valor;
    }

    public String getId() { return id; }
    public String getAsunto() { return asunto; }
    public String getContenido() { return contenido; }
    public TipoOrden getTipo() { return tipo; }
    public Confidencialidad getConfidencialidad() { return confidencialidad; }
    public Set<UnidadDestino> getDestinatarios() { return Set.copyOf(destinatarios); }
    public String getCreador() { return creador; }
    public String getClaveIdempotencia() { return claveIdempotencia; }
    public EstadoOrden getEstado() { return estado; }
    public Instant getCreadaEn() { return creadaEn; }
    public long getVersion() { return version; }
}
