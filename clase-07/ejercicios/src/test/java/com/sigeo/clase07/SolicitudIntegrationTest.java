package com.sigeo.clase07;

import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("postgres")
class SolicitudIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("sigeo_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @BeforeEach
    void setUp() {
        solicitudRepository.deleteAll();
    }

    @Test
    void flujoCompleto_CrearYActualizar() {
        // Arrange
        Solicitud solicitud = new Solicitud("Test Integración", "PENDIENTE");
        solicitud = solicitudRepository.save(solicitud);

        Solicitud updateRequest = new Solicitud("Test Actualizado", "PENDIENTE");

        // Act
        // TODO(C07-E06): El test fallará hasta que se implemente el endpoint PUT
        ResponseEntity<Solicitud> response = restTemplate.exchange(
                "/api/solicitudes/" + solicitud.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                Solicitud.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        Solicitud actualizada = solicitudRepository.findById(solicitud.getId()).orElseThrow();
        assertThat(actualizada.getDescripcion()).isEqualTo("Test Actualizado");
    }
}
