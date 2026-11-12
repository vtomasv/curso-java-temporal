package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e01|all")
class E01ModeloInvariantesTest {

    @Test
    void e01_exige_elegir_donde_viven_las_invariantes() {
        assertThat(Decisiones.DOMINIO)
                .as("EV03-E01: cambie DOMINIO y justifique la alternativa descartada")
                .isNotEqualTo(Decisiones.Dominio.PENDIENTE);
    }

    @Test
    void el_contenido_admite_entre_20_y_2000_caracteres_inclusive() {
        requiereDecision();

        assertThatThrownBy(() -> orden("x".repeat(19), Set.of(UnidadDestino.UNIDAD_ANDINA)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatCode(() -> orden("x".repeat(20), Set.of(UnidadDestino.UNIDAD_ANDINA)))
                .doesNotThrowAnyException();
        assertThatCode(() -> orden("x".repeat(2000), Set.of(UnidadDestino.UNIDAD_ANDINA)))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> orden("x".repeat(2001), Set.of(UnidadDestino.UNIDAD_ANDINA)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void una_orden_sin_destinatarios_no_puede_existir() {
        requiereDecision();

        assertThatThrownBy(() -> orden("contenido suficientemente largo", Set.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("destinatarios");
    }

    @Test
    void los_destinatarios_duplicados_se_rechazan_antes_de_convertirlos_a_set() {
        requiereDecision();

        assertThatThrownBy(() -> orden("contenido suficientemente largo", List.of(
                UnidadDestino.UNIDAD_ANDINA, UnidadDestino.UNIDAD_ANDINA)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duplicados");
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.DOMINIO == Decisiones.Dominio.PENDIENTE,
                "Primero complete DECISION(EV03-E01)");
    }

    private Orden orden(String contenido, Collection<UnidadDestino> destinos) {
        return new Orden("ORDEN-E01", "Asunto E01", contenido, TipoOrden.ORDEN,
                Confidencialidad.RESERVADA, destinos, "oficial1", "IDEM-E01");
    }
}
