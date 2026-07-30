package com.sigeo.clase05;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {
    Solicitud save(Solicitud solicitud);
    Optional<Solicitud> findById(Long id);
    List<Solicitud> findAll();
    List<Solicitud> findByEstadoAndPrioridad(String estado, String prioridad);
}
