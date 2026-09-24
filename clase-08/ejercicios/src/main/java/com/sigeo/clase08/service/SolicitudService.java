package com.sigeo.clase08.service;

import com.sigeo.clase08.model.Solicitud;
import com.sigeo.clase08.repository.SolicitudRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SolicitudService {
    private final SolicitudRepository repository;
    public SolicitudService(SolicitudRepository repository) { this.repository = repository; }

    @PreAuthorize("hasAnyRole('LECTOR', 'OPERADOR', 'SUPERVISOR')")
    public List<Solicitud> findAll() { return repository.findAll(); }

    @PreAuthorize("hasRole('OPERADOR')")
    public Solicitud create(String descripcion) {
        return repository.save(new Solicitud(descripcion,
            SecurityContextHolder.getContext().getAuthentication().getName(), "PENDIENTE"));
    }

    // E05: consulta al propietario persistido, nunca al propietario recibido del cliente.
    public boolean isOwner(Long id, String username) {
        return repository.findById(id).map(s -> username.equals(s.getPropietario())).orElse(false);
    }

    @PreAuthorize("hasRole('SUPERVISOR') or (hasRole('OPERADOR') and @solicitudService.isOwner(#id, authentication.name))")
    public Solicitud update(Long id, String descripcion) {
        var solicitud = required(id);
        solicitud.setDescripcion(descripcion);
        return repository.save(solicitud);
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    public void approve(Long id) {
        var solicitud = required(id);
        solicitud.setEstado("APROBADO");
        repository.save(solicitud);
    }

    private Solicitud required(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
