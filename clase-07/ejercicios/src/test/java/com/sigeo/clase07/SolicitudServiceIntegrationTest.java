package com.sigeo.clase07;

import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.AprobacionRepository;
import com.sigeo.clase07.repository.SolicitudRepository;
import com.sigeo.clase07.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
class SolicitudServiceIntegrationTest {

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private AprobacionRepository aprobacionRepository;

    private Long solicitudId;

    @BeforeEach
    void setUp() {
        aprobacionRepository.deleteAll();
        solicitudRepository.deleteAll();

        Solicitud solicitud = new Solicitud("Solicitud de prueba", "PENDIENTE");
        solicitud = solicitudRepository.save(solicitud);
        solicitudId = solicitud.getId();
    }

    @Test
    void testRollbackTotal() {
        // Act
        Throwable thrown = catchThrowable(() -> 
            solicitudService.aprobarSolicitud(solicitudId, "Admin", true)
        );

        // Assert
        assertThat(thrown).isNotNull();
        
        // TODO(C07-E01): El test fallará hasta que se configure correctamente @Transactional
        // La solicitud debe seguir en estado PENDIENTE
        Solicitud solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        assertThat(solicitud.getEstado()).isEqualTo("PENDIENTE");
        
        // No debe haber aprobaciones registradas
        assertThat(aprobacionRepository.findAll()).isEmpty();
    }
}
