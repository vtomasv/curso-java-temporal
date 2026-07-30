package com.sigeo.clase07;

import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.SolicitudRepository;
import com.sigeo.clase07.service.SelfInvocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
class SelfInvocationTest {

    @Autowired
    private SelfInvocationService selfInvocationService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    private Long solicitudId;

    @BeforeEach
    void setUp() {
        solicitudRepository.deleteAll();
        Solicitud solicitud = new Solicitud("Test Self Invocation", "PENDIENTE");
        solicitud = solicitudRepository.save(solicitud);
        solicitudId = solicitud.getId();
    }

    @Test
    void testSelfInvocationRollback() {
        // Act
        Throwable thrown = catchThrowable(() -> 
            selfInvocationService.procesarSolicitud(solicitudId, true)
        );

        // Assert
        assertThat(thrown).isNotNull();
        
        // TODO(C07-E02): El test fallará porque la llamada interna no pasa por el proxy transaccional
        // La solicitud debe seguir en estado PENDIENTE
        Solicitud solicitud = solicitudRepository.findById(solicitudId).orElseThrow();
        assertThat(solicitud.getEstado()).isEqualTo("PENDIENTE");
    }
}
