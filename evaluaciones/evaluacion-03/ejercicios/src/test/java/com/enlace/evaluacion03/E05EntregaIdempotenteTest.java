package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.workflow.DespachoActivitiesImpl;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e05|all")
class E05EntregaIdempotenteTest {

    @Test
    void e05_exige_elegir_una_clave_de_idempotencia() {
        assertThat(Decisiones.IDEMPOTENCIA)
                .as("EV03-E05: cambie IDEMPOTENCIA y documente la clave estable")
                .isNotEqualTo(Decisiones.Idempotencia.PENDIENTE);
    }

    @Test
    void un_reintento_equivalente_devuelve_el_mismo_recibo_sin_repetir_el_efecto() {
        requiereDecision();
        DespachoActivitiesImpl activities = new DespachoActivitiesImpl();

        ReciboEnvio primero = activities.enviar("ORDEN-E05", "UNIDAD_ANDINA", "CMD-E05");
        ReciboEnvio reintento = activities.enviar("ORDEN-E05", "UNIDAD_ANDINA", "CMD-E05");

        assertThat(reintento).isEqualTo(primero);
        assertThat(activities.efectosRegistrados()).isOne();
    }

    @Test
    void un_comando_explicito_no_puede_reutilizarse_con_otros_datos() {
        requiereDecision();
        assumeFalse(Decisiones.IDEMPOTENCIA != Decisiones.Idempotencia.COMANDO_EXPLICITO,
                "El conflicto de comando explícito no corresponde al camino de clave natural");
        DespachoActivitiesImpl activities = new DespachoActivitiesImpl();
        activities.enviar("ORDEN-E05", "UNIDAD_ANDINA", "CMD-COMPARTIDO");

        assertThatThrownBy(() -> activities.enviar(
                "ORDEN-E05", "UNIDAD_COSTERA", "CMD-COMPARTIDO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("comando");
        assertThat(activities.efectosRegistrados()).isOne();
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.IDEMPOTENCIA == Decisiones.Idempotencia.PENDIENTE,
                "Primero complete DECISION(EV03-E05)");
    }
}
