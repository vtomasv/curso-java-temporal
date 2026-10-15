package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.workflow.SolicitudActivitiesImpl;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflowInput;
import io.temporal.failure.ApplicationFailure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudActivitiesCasosLimiteTest {

    @Test
    void unaClaveRepetidaConOtrosDatosConservaElResultadoOriginal() {
        SolicitudActivitiesImpl activities = new SolicitudActivitiesImpl();

        activities.registrar(new SolicitudWorkflowInput("SOL-200", "Reponer equipo", "KEY-200"));
        String resultado = activities.registrar(
                new SolicitudWorkflowInput("SOL-201", "Otra descripción", "KEY-200"));

        assertThat(resultado).isEqualTo("REG-SOL-200");
        assertThat(activities.efectosRegistrados()).isEqualTo(1);
    }

    @Test
    void clavesDistintasRegistranEfectosIndependientesAunqueCoincidaElId() {
        SolicitudActivitiesImpl activities = new SolicitudActivitiesImpl();

        String primero = activities.registrar(
                new SolicitudWorkflowInput("SOL-200", "Reponer equipo", "KEY-200"));
        String segundo = activities.registrar(
                new SolicitudWorkflowInput("SOL-200", "Reponer equipo", "KEY-201"));

        assertThat(primero).isEqualTo("REG-SOL-200");
        assertThat(segundo).isEqualTo("REG-SOL-200");
        assertThat(activities.efectosRegistrados()).isEqualTo(2);
    }

    @ParameterizedTest
    @MethodSource("entradasInvalidas")
    void rechazaDatosInvalidosSinReintentarNiRegistrarEfectos(SolicitudWorkflowInput input) {
        SolicitudActivitiesImpl activities = new SolicitudActivitiesImpl();

        assertThatThrownBy(() -> activities.registrar(input))
                .isInstanceOfSatisfying(ApplicationFailure.class, failure -> {
                    assertThat(failure.getType()).isEqualTo("VALIDATION");
                    assertThat(failure.isNonRetryable()).isTrue();
                });
        assertThat(activities.efectosRegistrados()).isZero();
    }

    private static Stream<SolicitudWorkflowInput> entradasInvalidas() {
        return Stream.of(
                (SolicitudWorkflowInput) null,
                new SolicitudWorkflowInput(null, "Reponer equipo", "KEY-200"),
                new SolicitudWorkflowInput("", "Reponer equipo", "KEY-200"),
                new SolicitudWorkflowInput(" \t", "Reponer equipo", "KEY-200"),
                new SolicitudWorkflowInput("SOL-200", null, "KEY-200"),
                new SolicitudWorkflowInput("SOL-200", "", "KEY-200"),
                new SolicitudWorkflowInput("SOL-200", " \t", "KEY-200"),
                new SolicitudWorkflowInput("SOL-200", "Reponer equipo", null),
                new SolicitudWorkflowInput("SOL-200", "Reponer equipo", ""),
                new SolicitudWorkflowInput("SOL-200", "Reponer equipo", " \t"));
    }
}
