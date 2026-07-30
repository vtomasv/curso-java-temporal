package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AprobacionRelacionTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void debePersistirAprobacionEnCascada() {
        Solicitud solicitud = new Solicitud("Test", "PENDIENTE", 1);
        Aprobacion aprobacion = new Aprobacion("Admin", "OK");
        
        solicitud.addAprobacion(aprobacion);
        
        Solicitud guardada = entityManager.persistAndFlush(solicitud);
        entityManager.clear();
        
        Solicitud recuperada = entityManager.find(Solicitud.class, guardada.getId());
        assertThat(recuperada.getAprobaciones()).hasSize(1);
        assertThat(recuperada.getAprobaciones().get(0).getResponsable()).isEqualTo("Admin");
    }
}
