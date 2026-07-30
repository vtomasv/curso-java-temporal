package com.sigeo.clase05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudServiceTest {

    private SolicitudService solicitudService;
    private SolicitudRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySolicitudRepository();
        // El servicio debe aceptar el repositorio por constructor
        solicitudService = new SolicitudService(repository);
    }

    @Test
    void crearSolicitud_DebeGuardarConEstadoCreada() {
        Solicitud solicitud = solicitudService.crearSolicitud("Test", "Desc", "ALTA");
        
        assertThat(solicitud.getId()).isNotNull();
        assertThat(solicitud.getEstado()).isEqualTo("CREADA");
        assertThat(solicitud.getTitulo()).isEqualTo("Test");
        
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DebeLanzarExcepcion() {
        assertThatThrownBy(() -> solicitudService.obtenerPorId(999L))
                .isInstanceOf(SolicitudNotFoundException.class);
    }
}
