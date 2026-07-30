package com.sigeo.clase08.service;

import com.sigeo.clase08.model.Solicitud;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    public List<Solicitud> findAll() {
        return List.of(new Solicitud("Solicitud 1", "user1", "PENDIENTE"));
    }

    public Optional<Solicitud> findById(Long id) {
        return Optional.of(new Solicitud("Solicitud " + id, "user1", "PENDIENTE"));
    }

    public Solicitud create(Solicitud solicitud) {
        solicitud.setId(1L);
        return solicitud;
    }

    // TODO(C08-E05): Verificar que el solicitante edite su propia solicitud (salvo el supervisor).
    // Usar @PreAuthorize o lógica en el método.
    public Solicitud update(Long id, Solicitud solicitud) {
        throw new UnsupportedOperationException("TODO C08-E05");
    }

    public void approve(Long id) {
        // Lógica de aprobación
    }
}
