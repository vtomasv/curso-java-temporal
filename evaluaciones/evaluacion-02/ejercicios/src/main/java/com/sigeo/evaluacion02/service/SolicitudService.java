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
        var existente = this.repository.findByClaveIdempotencia(claveIdempotencia);
        if (existente.isPresent()) {
            return SolicitudResponse.from(existente.get());
        }

        Solicitud guardada = this.repository.save(new Solicitud(
                request.id(),
                request.solicitante(),
                request.descripcion(),
                request.prioridad(),
                propietario,
                claveIdempotencia
        ));
        return SolicitudResponse.from(guardada);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public SolicitudResponse buscarPorId(String id) {
        // TODO(EV02-E04): Buscar por id o lanzar SolicitudNoEncontradaException.
        Solicitud solicitud = this.repository.findById(id).orElseThrow(() -> new SolicitudNoEncontradaException(id));
        return SolicitudResponse.from(solicitud);
    }

    @Transactional
    @PreAuthorize("hasRole('SUPERVISOR')")
    public SolicitudResponse aprobar(String id) {
        // TODO(EV02-E04): Buscar, aplicar la transición de dominio y devolver el DTO.
        // JPA debe persistir el cambio dentro de la transacción.
        throw new UnsupportedOperationException("TODO EV02-E04");
    }
}
