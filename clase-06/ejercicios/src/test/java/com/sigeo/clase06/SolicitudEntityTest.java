package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SolicitudEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void debePersistirSolicitudConUUIDYTimestamps() {
        Solicitud solicitud = new Solicitud("Test", "PENDIENTE", 1);
        
        Solicitud guardada = entityManager.persistAndFlush(solicitud);
        
        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getFechaCreacion()).isNotNull();
        assertThat(guardada.getFechaActualizacion()).isNotNull();
        assertThat(guardada.getVersion()).isEqualTo(0L);
    }
}
