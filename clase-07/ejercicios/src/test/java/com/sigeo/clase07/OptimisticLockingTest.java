package com.sigeo.clase07;

import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.SolicitudRepository;
import com.sigeo.clase07.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
class OptimisticLockingTest {

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    private Long solicitudId;

    @BeforeEach
    void setUp() {
        solicitudRepository.deleteAll();
        Solicitud solicitud = new Solicitud("Test Optimistic Locking", "PENDIENTE");
        solicitud = solicitudRepository.save(solicitud);
        solicitudId = solicitud.getId();
    }

    @Test
    void testOptimisticLockingConflict() {
        // Arrange
        Solicitud cliente1 = solicitudRepository.findById(solicitudId).orElseThrow();
        Solicitud cliente2 = solicitudRepository.findById(solicitudId).orElseThrow();

        // Act
        cliente1.setDescripcion("Actualización 1");
        solicitudRepository.save(cliente1);

        // TODO(C07-E03): El test fallará hasta que se implemente @Version en Solicitud
        Throwable thrown = catchThrowable(() -> {
            cliente2.setDescripcion("Actualización 2");
            solicitudRepository.save(cliente2);
        });

        // Assert
        assertThat(thrown).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
