package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.workflow.DespachoInput;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import com.enlace.evaluacion03.workflow.ProgresoDespacho;
import com.enlace.evaluacion03.workflow.ResultadoDespacho;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e07|all")
class E07ConfirmacionIncertidumbreTest {

    private static final String ANDINA = "UNIDAD_ANDINA";
    private static final String COSTERA = "UNIDAD_COSTERA";
    private static final String AUSTRAL = "UNIDAD_AUSTRAL";
    private static final List<String> TRES_UNIDADES = List.of(ANDINA, COSTERA, AUSTRAL);

    @Test
    void e07_exige_elegir_confirmacion_e_incertidumbre() {
        assertThat(Decisiones.CONFIRMACION)
                .as("EV03-E07: cambie CONFIRMACION y documente la condición")
                .isNotEqualTo(Decisiones.Confirmacion.PENDIENTE);
        assertThat(Decisiones.INCERTIDUMBRE)
                .as("EV03-E07: cambie INCERTIDUMBRE y documente el tratamiento del timeout")
                .isNotEqualTo(Decisiones.Incertidumbre.PENDIENTE);
    }

    @Test
    void señales_duplicadas_no_alteran_el_progreso_y_la_politica_confirma_la_entrega() {
        requiereDecisiones();
        AtomicReference<ProgresoDespacho> progresoIntermedio = new AtomicReference<>();

        try (WorkflowTestSupport soporte = new WorkflowTestSupport()) {
            DespachoWorkflow workflow = soporte.nuevoWorkflow("workflow-e07-confirmaciones");
            soporte.despuesDe(Duration.ofHours(1), () -> {
                workflow.confirmarRecepcion(ANDINA, recibo(ANDINA));
                workflow.confirmarRecepcion(ANDINA, recibo(ANDINA));
            });
            soporte.despuesDe(Duration.ofHours(2),
                    () -> progresoIntermedio.set(workflow.consultarProgreso()));
            soporte.despuesDe(Duration.ofHours(3), () -> {
                workflow.confirmarRecepcion(COSTERA, recibo(COSTERA));
                if (Decisiones.CONFIRMACION == Decisiones.Confirmacion.TODAS_LAS_UNIDADES) {
                    workflow.confirmarRecepcion(AUSTRAL, recibo(AUSTRAL));
                }
            });

            ResultadoDespacho resultado = workflow.iniciar(
                    new DespachoInput("ORDEN-E07-CONF", TRES_UNIDADES));

            assertThat(progresoIntermedio.get()).isNotNull();
            assertThat(progresoIntermedio.get().confirmadas())
                    .as("dos Signals iguales representan una sola confirmación")
                    .isOne();
            assertThat(resultado.estado()).isEqualTo(EstadoDespacho.ENTREGADA);
            int confirmacionesEsperadas = Decisiones.CONFIRMACION
                    == Decisiones.Confirmacion.TODAS_LAS_UNIDADES ? 3 : 2;
            assertThat(resultado.confirmadas()).hasSize(confirmacionesEsperadas);
        }
    }

    @Test
    void al_vencer_el_plazo_solo_se_compensan_resultados_confirmados() {
        requiereDecisiones();

        try (WorkflowTestSupport soporte = new WorkflowTestSupport()) {
            DespachoWorkflow workflow = soporte.nuevoWorkflow("workflow-e07-timeout");
            soporte.despuesDe(Duration.ofHours(1),
                    () -> workflow.confirmarRecepcion(ANDINA, recibo(ANDINA)));

            ResultadoDespacho resultado = workflow.iniciar(
                    new DespachoInput("ORDEN-E07-TIMEOUT", TRES_UNIDADES));

            assertThat(resultado.estado()).isEqualTo(EstadoDespacho.PENDIENTE_REVISION);
            if (Decisiones.INCERTIDUMBRE == Decisiones.Incertidumbre.REVISION_MANUAL) {
                assertThat(soporte.activities.compensadas()).isEmpty();
            } else {
                assertThat(soporte.activities.compensadas())
                        .as("no se compensa una entrega cuyo resultado se desconoce")
                        .isEqualTo(Set.of(ANDINA));
            }
        }
    }

    @Test
    void una_cancelacion_por_update_termina_de_forma_controlada() {
        requiereDecisiones();
        AtomicBoolean aceptada = new AtomicBoolean();

        try (WorkflowTestSupport soporte = new WorkflowTestSupport()) {
            DespachoWorkflow workflow = soporte.nuevoWorkflow("workflow-e07-cancelacion");
            soporte.despuesDe(Duration.ofHours(1),
                    () -> aceptada.set(workflow.cancelar("cambio de prioridad")));

            ResultadoDespacho resultado = workflow.iniciar(
                    new DespachoInput("ORDEN-E07-CANCEL", TRES_UNIDADES));

            assertThat(aceptada).hasValue(true);
            assertThat(resultado.estado()).isEqualTo(EstadoDespacho.CANCELADA);
        }
    }

    private void requiereDecisiones() {
        assumeFalse(Decisiones.CONFIRMACION == Decisiones.Confirmacion.PENDIENTE
                        || Decisiones.INCERTIDUMBRE == Decisiones.Incertidumbre.PENDIENTE,
                "Primero complete las dos decisiones de EV03-E07");
    }

    private String recibo(String unidad) {
        return "REC-" + unidad;
    }
}
