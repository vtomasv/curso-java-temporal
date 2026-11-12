package com.enlace.evaluacion03;

import com.enlace.evaluacion03.workflow.DespachoActivitiesImpl;
import com.enlace.evaluacion03.workflow.DespachoInput;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "base|all")
class BaseTest {

    @Test
    void la_plantilla_inicia_con_un_contrato_de_despacho_valido() {
        DespachoInput input = new DespachoInput("  ORDEN-BASE  ",
                List.of("  UNIDAD_ANDINA  ", "UNIDAD_COSTERA"));

        assertThat(input.ordenId()).isEqualTo("ORDEN-BASE");
        assertThat(input.unidades()).containsExactly("UNIDAD_ANDINA", "UNIDAD_COSTERA");
        assertThatThrownBy(() -> new DespachoInput("ORDEN-BASE", List.of("UNIDAD_ANDINA", " UNIDAD_ANDINA ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duplicadas");
    }

    @Test
    void el_camino_seguro_inicial_es_idempotente_y_compensable() {
        DespachoActivitiesImpl activities = new DespachoActivitiesImpl();

        ReciboEnvio primero = activities.enviar("ORDEN-BASE", "UNIDAD_ANDINA", "CMD-1");
        ReciboEnvio reintento = activities.enviar("ORDEN-BASE", "UNIDAD_ANDINA", "CMD-1");

        assertThat(reintento).isEqualTo(primero);
        assertThat(activities.efectosRegistrados()).isOne();
        assertThat(activities.recibosActivos()).isOne();

        activities.compensar("ORDEN-BASE", "UNIDAD_ANDINA", primero.reciboId());
        activities.compensar("ORDEN-BASE", "UNIDAD_ANDINA", primero.reciboId());
        assertThat(activities.recibosActivos()).isZero();
    }
}
