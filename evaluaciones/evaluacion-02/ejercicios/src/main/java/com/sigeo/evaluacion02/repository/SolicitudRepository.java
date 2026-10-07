package com.sigeo.evaluacion02.repository;

import com.sigeo.evaluacion02.domain.EstadoSolicitud;
import com.sigeo.evaluacion02.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, String> {

    // Contratos entregados: Spring Data implementa ambas consultas por su nombre.
    Optional<Solicitud> findByClaveIdempotencia(String claveIdempotencia);

    List<Solicitud> findByEstadoOrderByIdAsc(EstadoSolicitud estado);
}
