package com.sigeo.clase07.service;

import com.sigeo.clase07.domain.Aprobacion;
import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.AprobacionRepository;
import com.sigeo.clase07.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

@Service
public class AprobacionService {

    private final SolicitudRepository solicitudRepository;
    private final AprobacionRepository aprobacionRepository;

    public AprobacionService(SolicitudRepository solicitudRepository, AprobacionRepository aprobacionRepository) {
        this.solicitudRepository = solicitudRepository;
        this.aprobacionRepository = aprobacionRepository;
    }

    public Aprobacion registrarAprobacion(Long solicitudId, String aprobador, String comentarios) {
        // TODO(C07-E04): Implementar lógica de registro de aprobación
        // 1. Buscar solicitud
        // 2. Validar que no esté ya aprobada
        // 3. Crear y guardar aprobación
        // 4. Actualizar estado de solicitud
        throw new UnsupportedOperationException("TODO C07-E04");
    }
}
