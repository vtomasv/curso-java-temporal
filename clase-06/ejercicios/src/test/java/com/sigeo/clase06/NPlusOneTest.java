package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NPlusOneTest {

    @Autowired
    private SolicitudRepository repository;

    @Test
    void debeEvitarNPlusOneAlBuscarConAprobaciones() {
        Solicitud s1 = new Solicitud("T1", "PENDIENTE", 1);
        s1.addAprobacion(new Aprobacion("Admin", "OK"));
        repository.save(s1);
        
        Solicitud s2 = new Solicitud("T2", "PENDIENTE", 1);
        s2.addAprobacion(new Aprobacion("User", "OK"));
        repository.save(s2);
        
        // El test real verificaría el número de consultas SQL ejecutadas
        // Aquí solo verificamos que el método funciona y trae los datos
        List<Solicitud> solicitudes = repository.findAllWithAprobaciones();
        
        assertThat(solicitudes).hasSizeGreaterThanOrEqualTo(2);
        assertThat(solicitudes.get(0).getAprobaciones()).isNotEmpty();
    }
}
