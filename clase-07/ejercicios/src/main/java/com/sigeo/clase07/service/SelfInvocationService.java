package com.sigeo.clase07.service;

import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelfInvocationService {

    private final SolicitudRepository solicitudRepository;

    public SelfInvocationService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void procesarSolicitud(Long id, boolean simularFallo) {
        actualizarEstado(id, simularFallo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void actualizarEstado(Long id, boolean simularFallo) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        
        solicitud.setEstado("PROCESADA");
        solicitudRepository.save(solicitud);
        
        if (simularFallo) {
            throw new RuntimeException("Fallo simulado");
        }
    }
}
