package com.sigeo.clase06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SolicitudPaginacionTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SolicitudRepository repository;

    @Test
    void debeRetornarPaginaDeSolicitudes() {
        repository.save(new Solicitud("T1", "PENDIENTE", 1));
        repository.save(new Solicitud("T2", "PENDIENTE", 1));
        
        ResponseEntity<PageDTO> response = restTemplate.getForEntity("/api/solicitudes?estado=PENDIENTE&page=0&size=1", PageDTO.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).hasSize(1);
        assertThat(response.getBody().totalElements()).isGreaterThanOrEqualTo(2);
    }
}
