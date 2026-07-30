package com.sigeo.clase06;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional
    public Solicitud crearSolicitud(String descripcion, String estado, Integer prioridad, Contacto contacto) {
        Solicitud solicitud = new Solicitud(descripcion, estado, prioridad);
        solicitud.setContacto(contacto);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public void agregarAprobacion(UUID solicitudId, String responsable, String comentario) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        
        Aprobacion aprobacion = new Aprobacion(responsable, comentario);
        solicitud.addAprobacion(aprobacion);
        // No es necesario llamar a save() si la relación tiene cascade y estamos en una transacción
    }

    @Transactional(readOnly = true)
    public Page<Solicitud> listarPorEstadoPaginado(String estado, Pageable pageable) {
        return solicitudRepository.findByEstado(estado, pageable);
    }

    @Transactional(readOnly = true)
    public List<Solicitud> listarTodasConAprobaciones() {
        return solicitudRepository.findAllWithAprobaciones();
    }
}
