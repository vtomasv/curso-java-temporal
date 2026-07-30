package com.sigeo.clase07;

import com.sigeo.clase07.domain.Aprobacion;
import com.sigeo.clase07.domain.Solicitud;
import com.sigeo.clase07.repository.AprobacionRepository;
import com.sigeo.clase07.repository.SolicitudRepository;
import com.sigeo.clase07.service.AprobacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AprobacionServiceTest {

    private SolicitudRepository solicitudRepository;
    private AprobacionRepository aprobacionRepository;
    private AprobacionService aprobacionService;

    @BeforeEach
    void setUp() {
        solicitudRepository = Mockito.mock(SolicitudRepository.class);
        aprobacionRepository = Mockito.mock(AprobacionRepository.class);
        aprobacionService = new AprobacionService(solicitudRepository, aprobacionRepository);
    }

    @Test
    void registrarAprobacion_Exito() {
        // Arrange
        Solicitud solicitud = new Solicitud("Test", "PENDIENTE");
        solicitud.setId(1L);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));
        
        Aprobacion aprobacionGuardada = new Aprobacion(1L, "Admin", "OK");
        aprobacionGuardada.setId(100L);
        when(aprobacionRepository.save(any(Aprobacion.class))).thenReturn(aprobacionGuardada);

        // Act
        // TODO(C07-E04): El test fallará hasta que se implemente registrarAprobacion
        Aprobacion resultado = aprobacionService.registrarAprobacion(1L, "Admin", "OK");

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(100L);
        assertThat(solicitud.getEstado()).isEqualTo("APROBADA");
    }
    
    @Test
    void registrarAprobacion_YaAprobada_LanzaExcepcion() {
        // Arrange
        Solicitud solicitud = new Solicitud("Test", "APROBADA");
        solicitud.setId(1L);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        // Act
        Throwable thrown = catchThrowable(() -> 
            aprobacionService.registrarAprobacion(1L, "Admin", "OK")
        );

        // Assert
        assertThat(thrown)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("ya está aprobada");
    }
}
