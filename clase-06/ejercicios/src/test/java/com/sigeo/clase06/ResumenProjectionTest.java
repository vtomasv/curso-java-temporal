package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ResumenProjectionTest {

    @Autowired
    private SolicitudRepository repository;

    @Test
    void debeRetornarResumenPorResponsable() {
        Solicitud s = new Solicitud("Test", "PENDIENTE", 1);
        s.addAprobacion(new Aprobacion("Admin", "OK 1"));
        s.addAprobacion(new Aprobacion("Admin", "OK 2"));
        s.addAprobacion(new Aprobacion("User", "OK"));
        repository.save(s);
        
        List<ResumenResponsable> resumen = repository.obtenerResumenPorResponsable();
        
        assertThat(resumen).hasSize(2);
        
        ResumenResponsable adminResumen = resumen.stream()
                .filter(r -> "Admin".equals(r.getResponsable()))
                .findFirst()
                .orElseThrow();
                
        assertThat(adminResumen.getCantidadAprobaciones()).isEqualTo(2L);
        assertThat(adminResumen.getUltimaAprobacion()).isNotNull();
    }
}
