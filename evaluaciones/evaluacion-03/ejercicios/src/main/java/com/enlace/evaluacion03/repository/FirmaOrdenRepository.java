package com.enlace.evaluacion03.repository;

import com.enlace.evaluacion03.domain.FirmaOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FirmaOrdenRepository extends JpaRepository<FirmaOrden, Long> {
    boolean existsByOrdenIdAndFirmante(String ordenId, String firmante);
    long countByOrdenId(String ordenId);
    List<FirmaOrden> findByOrdenIdOrderByFirmadaEnAsc(String ordenId);
}
