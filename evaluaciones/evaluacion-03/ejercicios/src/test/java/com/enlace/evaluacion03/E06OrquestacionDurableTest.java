package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.workflow.DespachoInput;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import com.enlace.evaluacion03.workflow.ResultadoDespacho;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e06|all")
class E06OrquestacionDurableTest {

    private static final List<String> UNIDADES = List.of(
            "UNIDAD_ANDINA", "UNIDAD_COSTERA", "UNIDAD_AUSTRAL");

    @Test
    void e06_exige_elegir_una_orquestacion_durable() {
        assertThat(Decisiones.ORQUESTACION)
                .as("EV03-E06: cambie ORQUESTACION y justifique secuencia, paralelismo o hijos")
                .isNotEqualTo(Decisiones.Orquestacion.PENDIENTE);
    }

    @Test
    void el_workflow_entrega_una_vez_a_cada_unidad_y_termina_sin_bloquear() {
        requiereDecision();

        try (WorkflowTestSupport soporte = new WorkflowTestSupport()) {
            DespachoWorkflow workflow = soporte.nuevoWorkflow("workflow-e06");
            ResultadoDespacho resultado = workflow.iniciar(new DespachoInput("ORDEN-E06", UNIDADES));

            assertThat(resultado.estado()).isEqualTo(EstadoDespacho.PENDIENTE_REVISION);
            assertThat(resultado.recibos()).hasSize(UNIDADES.size());
            assertThat(resultado.recibos().stream().map(ReciboEnvio::unidad).collect(Collectors.toSet()))
                    .containsExactlyInAnyOrderElementsOf(UNIDADES);
            assertThat(soporte.activities.unidadesEntregadas())
                    .isEqualTo(Set.copyOf(UNIDADES));

            if (Decisiones.ORQUESTACION == Decisiones.Orquestacion.SECUENCIAL) {
                assertThat(soporte.activities.ordenDeEnvio())
                        .as("el camino secuencial conserva el orden declarado")
                        .containsExactlyElementsOf(UNIDADES);
            }
        }
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.ORQUESTACION == Decisiones.Orquestacion.PENDIENTE,
                "Primero complete DECISION(EV03-E06)");
    }
}
