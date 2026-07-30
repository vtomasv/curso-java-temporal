package com.sigeo.clase16.workflow;

import com.sigeo.clase16.domain.Solicitud;
import com.sigeo.clase16.repository.SolicitudRepository;
import org.springframework.stereotype.Component;

@Component
public class SolicitudActivitiesImpl implements SolicitudActivities {

    private final SolicitudRepository repository;

    public SolicitudActivitiesImpl(SolicitudRepository repository) {
        this.repository = repository;
    }

    @Override
    public void guardarEstadoSolicitud(Long solicitudId, String estado) {
        // TODO(C16-E08): Implementar la actividad
        // 1. Buscar la solicitud por ID
        // 2. Actualizar el estado
        // 3. Guardar en el repositorio
        throw new UnsupportedOperationException("TODO C16-E08: Implementar guardarEstadoSolicitud");
    }
}
