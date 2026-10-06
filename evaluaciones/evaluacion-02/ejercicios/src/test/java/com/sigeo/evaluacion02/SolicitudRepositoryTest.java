package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.domain.EstadoSolicitud;
import com.sigeo.evaluacion02.domain.Prioridad;
import com.sigeo.evaluacion02.domain.Solicitud;
import com.sigeo.evaluacion02.repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository repository;

    @Test
    void buscaPorClaveDeIdempotencia() {
        repository.save(solicitud("SOL-200", "KEY-200"));

        assertThat(repository.findByClaveIdempotencia("KEY-200"))
                .get()
                .extracting(Solicitud::getId)
                .isEqualTo("SOL-200");
    }

    @Test
    void filtraPorEstadoYOrdenaPorId() {
        Solicitud segunda = solicitud("SOL-202", "KEY-202");
        segunda.aprobar();
        repository.save(solicitud("SOL-201", "KEY-201"));
        repository.save(segunda);
        repository.save(solicitud("SOL-200", "KEY-200"));

        assertThat(repository.findByEstadoOrderByIdAsc(EstadoSolicitud.PENDIENTE))
                .extracting(Solicitud::getId)
                .containsExactly("SOL-200", "SOL-201");
    }

    private static Solicitud solicitud(String id, String clave) {
        return new Solicitud(
                id, "Cabo Rojas", "Reponer equipo",
                Prioridad.ALTA, "operador", clave);
    }
}
