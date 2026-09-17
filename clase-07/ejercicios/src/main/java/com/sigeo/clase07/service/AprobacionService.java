package com.sigeo.clase07.service;

import com.sigeo.clase07.domain.Aprobacion;
import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.AprobacionRepository;
import com.sigeo.clase07.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AprobacionService {

    private final SolicitudRepository solicitudRepository;
    private final AprobacionRepository aprobacionRepository;

    public AprobacionService(SolicitudRepository solicitudRepository, AprobacionRepository aprobacionRepository) {
        this.solicitudRepository = solicitudRepository;
        this.aprobacionRepository = aprobacionRepository;
    }

    @Transactional
    public Aprobacion registrarAprobacion(Long solicitudId, String aprobador, String comentarios) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        if ("APROBADA".equals(solicitud.getEstado())) {
            throw new IllegalStateException("La solicitud " + solicitudId + " ya está aprobada");
        }

        Aprobacion aprobacion = new Aprobacion(solicitudId, aprobador, comentarios);
        Aprobacion aprobacionGuardada = aprobacionRepository.save(aprobacion);

        solicitud.setEstado("APROBADA");
        solicitudRepository.save(solicitud);

        return aprobacionGuardada;
    }
}
