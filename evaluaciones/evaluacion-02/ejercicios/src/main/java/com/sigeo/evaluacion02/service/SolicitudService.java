package com.sigeo.evaluacion02.service;

import com.sigeo.evaluacion02.api.CrearSolicitudRequest;
import com.sigeo.evaluacion02.api.SolicitudResponse;
import com.sigeo.evaluacion02.domain.Solicitud;
import com.sigeo.evaluacion02.repository.SolicitudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudService {

    private final SolicitudRepository repository;

    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @PreAuthorize("hasRole('OPERADOR')")
    public SolicitudResponse crear(
            CrearSolicitudRequest request,
            String propietario,
            String claveIdempotencia
    ) {
        // TODO(EV02-E04): Si la clave ya existe, devolver la solicitud previa.
        // Si no existe, crear, guardar y mapear la entidad.
        throw new UnsupportedOperationException("TODO EV02-E04");
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public SolicitudResponse buscarPorId(String id) {
        // TODO(EV02-E04): Buscar por id o lanzar SolicitudNoEncontradaException.
        throw new UnsupportedOperationException("TODO EV02-E04");
    }

    @Transactional
    @PreAuthorize("hasRole('SUPERVISOR')")
    public SolicitudResponse aprobar(String id) {
        // TODO(EV02-E04): Buscar, aplicar la transición de dominio y devolver el DTO.
        // JPA debe persistir el cambio dentro de la transacción.
        throw new UnsupportedOperationException("TODO EV02-E04");
    }
}
