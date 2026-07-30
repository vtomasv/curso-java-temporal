package com.sigeo.clase07.repository;

import com.sigeo.clase07.domain.Aprobacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AprobacionRepository extends JpaRepository<Aprobacion, Long> {
}
