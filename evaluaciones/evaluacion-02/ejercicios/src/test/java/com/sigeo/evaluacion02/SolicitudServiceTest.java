package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.api.CrearSolicitudRequest;
import com.sigeo.evaluacion02.domain.Prioridad;
import com.sigeo.evaluacion02.domain.Solicitud;
import com.sigeo.evaluacion02.repository.SolicitudRepository;
import com.sigeo.evaluacion02.service.SolicitudNoEncontradaException;
import com.sigeo.evaluacion02.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitudServiceTest {

    private SolicitudRepository repository;
    private SolicitudService service;

    @BeforeEach
    void setUp() {
        repository = mock(SolicitudRepository.class);
        service = new SolicitudService(repository);
    }

    @Test
    void creaYMapeaUnaSolicitudNueva() {
        when(repository.findByClaveIdempotencia("KEY-200")).thenReturn(Optional.empty());
        when(repository.save(any(Solicitud.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.crear(request(), "operador", "KEY-200");

        assertThat(response.id()).isEqualTo("SOL-200");
        assertThat(response.propietario()).isEqualTo("operador");
        ArgumentCaptor<Solicitud> captor = ArgumentCaptor.forClass(Solicitud.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getClaveIdempotencia()).isEqualTo("KEY-200");
    }

    @Test
    void unaClaveRepetidaDevuelveElResultadoPrevioSinGuardarOtraVez() {
        Solicitud existente = new Solicitud(
                "SOL-ANT", "Cabo Rojas", "Previa", Prioridad.MEDIA, "operador", "KEY-200");
        when(repository.findByClaveIdempotencia("KEY-200")).thenReturn(Optional.of(existente));

        var response = service.crear(request(), "operador", "KEY-200");

        assertThat(response.id()).isEqualTo("SOL-ANT");
        verify(repository, never()).save(any());
    }

    @Test
    void buscarUnIdAusenteEntregaErrorDeDominio() {
        when(repository.findById("SOL-404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId("SOL-404"))
                .isInstanceOf(SolicitudNoEncontradaException.class)
                .hasMessageContaining("SOL-404");
    }

    private static CrearSolicitudRequest request() {
        return new CrearSolicitudRequest(
                "SOL-200", "Cabo Rojas", "Reponer equipo", Prioridad.ALTA);
    }
}

