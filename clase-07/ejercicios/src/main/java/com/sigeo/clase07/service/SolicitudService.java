package com.sigeo.clase07.service;

import com.sigeo.clase07.domain.Aprobacion;
import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.AprobacionRepository;
import com.sigeo.clase07.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final AprobacionRepository aprobacionRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, AprobacionRepository aprobacionRepository) {
        this.solicitudRepository = solicitudRepository;
        this.aprobacionRepository = aprobacionRepository;
    }

    // TODO(C07-E01): Configurar la transacción para que haga rollback ante cualquier excepción
    public void aprobarSolicitud(Long solicitudId, String aprobador, boolean simularFallo) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        solicitud.setEstado("APROBADA");
        solicitudRepository.save(solicitud);

        Aprobacion aprobacion = new Aprobacion(solicitudId, aprobador, "Aprobado automáticamente");
        aprobacionRepository.save(aprobacion);

        if (simularFallo) {
            throw new RuntimeException("Fallo simulado durante la aprobación");
        }
    }
    
    public Solicitud actualizarSolicitud(Long id, String nuevaDescripcion) {
        // TODO(C07-E03): Implementar actualización de solicitud para probar Optimistic Locking
        throw new UnsupportedOperationException("TODO C07-E03");
    }
}
