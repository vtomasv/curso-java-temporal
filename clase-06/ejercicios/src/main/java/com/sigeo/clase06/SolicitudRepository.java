package com.sigeo.clase06;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {

    // TODO(C06-E03): Consulta derivada para buscar por estado y prioridad mayor o igual a X
    List<Solicitud> findByEstadoAndPrioridadGreaterThanEqual(String estado, Integer prioridad);

    // TODO(C06-E03): Consulta derivada para buscar creadas entre dos fechas
    List<Solicitud> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    // TODO(C06-E05): Consulta paginada por estado
    Page<Solicitud> findByEstado(String estado, Pageable pageable);

    // TODO(C06-E06): Consulta JPQL agregada que devuelve la proyección ResumenResponsable
    // Contar aprobaciones y obtener la fecha máxima por responsable
    @Query("SELECT a.responsable AS responsable, COUNT(a) AS cantidadAprobaciones, MAX(a.fecha) AS ultimaAprobacion " +
           "FROM Aprobacion a GROUP BY a.responsable")
    List<ResumenResponsable> obtenerResumenPorResponsable();

    // TODO(C06-E07): Resolver problema N+1 al buscar todas las solicitudes con sus aprobaciones
    // Usar @EntityGraph o @Query con JOIN FETCH
    @Query("SELECT s FROM Solicitud s")
    List<Solicitud> findAllWithAprobaciones();
}
