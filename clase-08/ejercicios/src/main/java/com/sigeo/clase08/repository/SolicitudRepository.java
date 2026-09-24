package com.sigeo.clase08.repository;
import com.sigeo.clase08.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {}
