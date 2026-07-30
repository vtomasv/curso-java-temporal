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

    public void procesarSolicitud(Long id, boolean simularFallo) {
        // TODO(C07-E02): Arreglar el problema de self-invocation
        // Actualmente, llamar a este método no inicia una transacción para actualizarEstado
        actualizarEstado(id, simularFallo);
    }

    @Transactional
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
