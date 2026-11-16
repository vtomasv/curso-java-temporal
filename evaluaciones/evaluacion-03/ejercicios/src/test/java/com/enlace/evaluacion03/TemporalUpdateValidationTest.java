package com.enlace.evaluacion03;

import com.enlace.evaluacion03.workflow.AccesoProgreso;
import com.enlace.evaluacion03.workflow.AccesoWorkflow;
import com.enlace.evaluacion03.workflow.DespachoInput;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import io.temporal.client.WorkflowClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Timeout(10)
@EnabledIfSystemProperty(named = "ev03.exercise", matches = "base|all")
class TemporalUpdateValidationTest {

    @Test
    void una_firma_duplicada_rechaza_solo_el_update_y_el_workflow_continua() {
        try (WorkflowTestSupport soporte = new WorkflowTestSupport(false)) {
            DespachoWorkflow workflow = soporte.nuevoWorkflow("despacho-validacion-firma");
            WorkflowClient.start(workflow::iniciar,
                    new DespachoInput("ORD-VALIDACION-FIRMA", List.of("UNIDAD_ANDINA")));

            assertThat(workflow.registrarFirma("oficial1", "OFICIAL").completa()).isFalse();
            assertThatThrownBy(() -> workflow.registrarFirma("oficial1", "OFICIAL"))
                    .rootCause()
                    .hasMessageContaining("ya firmó");

            assertThat(workflow.registrarFirma("comandante1", "COMANDANTE").completa())
                    .as("un Update rechazado no deja al Workflow Task en reintento")
                    .isTrue();
            assertThat(workflow.consultarFirmas().firmantes())
                    .containsExactlyInAnyOrder("oficial1", "comandante1");
        }
    }

    @Test
    void un_conflicto_de_idempotencia_rechaza_solo_el_update_de_acceso() {
        try (WorkflowTestSupport soporte = new WorkflowTestSupport(false)) {
            AccesoWorkflow workflow = soporte.nuevoWorkflowAccesos("accesos-validacion");
            WorkflowClient.start(workflow::mantener, "ORD-VALIDACION-ACCESO");

            workflow.registrarAcceso("ACCESO-1", "auditor", false);
            assertThatThrownBy(() ->
                    workflow.registrarAcceso("ACCESO-1", "comandante1", true))
                    .rootCause()
                    .hasMessageContaining("otra lectura");

            AccesoProgreso progreso = workflow.registrarAcceso(
                    "ACCESO-2", "comandante1", true);
            assertThat(progreso.accesosAutorizados()).isEqualTo(2);
            assertThat(progreso.reautenticaciones()).isOne();
            assertThat(workflow.consultarAccesos()).isEqualTo(progreso);
        }
    }
}
