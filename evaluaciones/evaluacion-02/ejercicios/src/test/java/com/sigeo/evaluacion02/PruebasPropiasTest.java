package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.api.CrearSolicitudRequest;
import com.sigeo.evaluacion02.api.SolicitudResponse;
import com.sigeo.evaluacion02.domain.EstadoSolicitud;
import com.sigeo.evaluacion02.domain.Prioridad;
import com.sigeo.evaluacion02.domain.Solicitud;
import com.sigeo.evaluacion02.repository.SolicitudRepository;
import com.sigeo.evaluacion02.service.SolicitudService;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pruebas propias de idempotencia y manejo de fallos de persistencia. */
class PruebasPropiasTest {

    @Test
    void unaClaveRepetidaConservaLosDatosYElEstadoDeLaSolicitudPrevia() {
        SolicitudRepository repository = mock(SolicitudRepository.class);
        SolicitudService service = new SolicitudService(repository);
        Solicitud existente = new Solicitud(
                "SOL-PREVIA", "Cabo Rojas", "Reponer equipo", Prioridad.MEDIA, "operador", "KEY-300");
        existente.aprobar();
        when(repository.findByClaveIdempotencia("KEY-300")).thenReturn(Optional.of(existente));
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "SOL-NUEVA", "Cabo Pérez", "Otra descripción", Prioridad.ALTA);

        var response = service.crear(request, "operador", "KEY-300");

        assertThat(response).isEqualTo(new SolicitudResponse(
                "SOL-PREVIA", "Cabo Rojas", "Reponer equipo", Prioridad.MEDIA,
                EstadoSolicitud.APROBADA, "operador", 0L));
        verify(repository, never()).save(any(Solicitud.class));
    }

    @Test
    void unFalloAlGuardarSePropagaSinDevolverUnaCreacionExitosa() {
        SolicitudRepository repository = mock(SolicitudRepository.class);
        SolicitudService service = new SolicitudService(repository);
        CrearSolicitudRequest request = new CrearSolicitudRequest(
                "SOL-301", "Cabo Rojas", "Reponer equipo", Prioridad.ALTA);
        DataAccessResourceFailureException fallo = new DataAccessResourceFailureException(
                "Base de datos no disponible");
        when(repository.findByClaveIdempotencia("KEY-301")).thenReturn(Optional.empty());
        when(repository.save(any(Solicitud.class))).thenThrow(fallo);

        assertThatThrownBy(() -> service.crear(request, "operador", "KEY-301"))
                .isSameAs(fallo);
        verify(repository).save(any(Solicitud.class));
    }
}
