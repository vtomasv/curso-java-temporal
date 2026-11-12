package com.enlace.evaluacion03.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(name = "firmas_orden", uniqueConstraints = @UniqueConstraint(columnNames = {"orden_id", "firmante"}))
public class FirmaOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_id", nullable = false)
    private String ordenId;

    @Column(nullable = false)
    private String firmante;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private Instant firmadaEn;

    protected FirmaOrden() {
    }

    public FirmaOrden(String ordenId, String firmante, String rol) {
        this.ordenId = texto(ordenId, "ordenId");
        this.firmante = texto(firmante, "firmante");
        this.rol = texto(rol, "rol");
        this.firmadaEn = Instant.now();
    }

    private static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException(campo);
        return valor.trim();
    }

    public Long getId() { return id; }
    public String getOrdenId() { return ordenId; }
    public String getFirmante() { return firmante; }
    public String getRol() { return rol; }
    public Instant getFirmadaEn() { return firmadaEn; }
}
