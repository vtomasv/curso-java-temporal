package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository repository;

    @Test
    void debeBuscarPorEstadoYPrioridad() {
        repository.save(new Solicitud("T1", "PENDIENTE", 5));
        repository.save(new Solicitud("T2", "PENDIENTE", 2));
        repository.save(new Solicitud("T3", "APROBADO", 5));
        
        List<Solicitud> resultados = repository.findByEstadoAndPrioridadGreaterThanEqual("PENDIENTE", 3);
        
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getDescripcion()).isEqualTo("T1");
    }

    @Test
    void debeBuscarPorRangoDeFechas() {
        Solicitud s = new Solicitud("T1", "PENDIENTE", 1);
        repository.save(s);
        
        LocalDateTime ahora = LocalDateTime.now();
        List<Solicitud> resultados = repository.findByFechaCreacionBetween(ahora.minusDays(1), ahora.plusDays(1));
        
        assertThat(resultados).hasSize(1);
    }
}
