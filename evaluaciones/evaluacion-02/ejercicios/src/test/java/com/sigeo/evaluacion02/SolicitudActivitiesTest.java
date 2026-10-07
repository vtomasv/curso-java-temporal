package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.workflow.SolicitudActivitiesImpl;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflowInput;
import io.temporal.failure.ApplicationFailure;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudActivitiesTest {

    @Test
    void unaClaveRepetidaRetornaElMismoResultadoSinRepetirElEfecto() {
        SolicitudActivitiesImpl activities = new SolicitudActivitiesImpl();
        SolicitudWorkflowInput input = input("SOL-200", "KEY-200");

        String primero = activities.registrar(input);
        String segundo = activities.registrar(input);

        assertThat(primero).isEqualTo("REG-SOL-200");
        assertThat(segundo).isEqualTo(primero);
        assertThat(activities.efectosRegistrados()).isEqualTo(1);
    }

    @Test
    void datosInvalidosGeneranFalloTipadoNoReintentable() {
        SolicitudActivitiesImpl activities = new SolicitudActivitiesImpl();

        assertThatThrownBy(() -> activities.registrar(input(" ", "KEY-200")))
                .isInstanceOf(ApplicationFailure.class)
                .satisfies(error -> {
                    ApplicationFailure failure = (ApplicationFailure) error;
                    assertThat(failure.getType()).isEqualTo("VALIDATION");
                });
    }

    private static SolicitudWorkflowInput input(String id, String clave) {
        return new SolicitudWorkflowInput(id, "Reponer equipo", clave);
    }
}

