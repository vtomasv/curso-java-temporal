package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.domain.EstadoSolicitud;
import com.sigeo.evaluacion02.domain.Prioridad;
import com.sigeo.evaluacion02.domain.Solicitud;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudDominioTest {

    @Test
    void unaSolicitudValidaComienzaPendiente() {
        Solicitud solicitud = solicitudValida();

        assertThat(solicitud.getId()).isEqualTo("SOL-200");
        assertThat(solicitud.getEstado()).isEqualTo(EstadoSolicitud.PENDIENTE);
        assertThat(solicitud.getPrioridad()).isEqualTo(Prioridad.ALTA);
    }

    @Test
    void rechazaDatosObligatoriosInvalidosConContexto() {
        assertThatThrownBy(() -> new Solicitud(
                " ", "Cabo Rojas", "Reponer equipo", Prioridad.ALTA, "operador", "KEY-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id");

        assertThatThrownBy(() -> new Solicitud(
                "SOL-200", "Cabo Rojas", " ", Prioridad.ALTA, "operador", "KEY-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("descripcion");

        assertThatThrownBy(() -> new Solicitud(
                "SOL-200", "Cabo Rojas", "Reponer equipo", null, "operador", "KEY-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("prioridad");
    }

    @Test
    void aprobarRealizaUnaTransicionUnica() {
        Solicitud solicitud = solicitudValida();

        solicitud.aprobar();

        assertThat(solicitud.getEstado()).isEqualTo(EstadoSolicitud.APROBADA);
        assertThatThrownBy(solicitud::aprobar)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("SOL-200");
    }

    private static Solicitud solicitudValida() {
        return new Solicitud(
                "SOL-200", "Cabo Rojas", "Reponer equipo",
                Prioridad.ALTA, "operador", "KEY-1");
    }
}

