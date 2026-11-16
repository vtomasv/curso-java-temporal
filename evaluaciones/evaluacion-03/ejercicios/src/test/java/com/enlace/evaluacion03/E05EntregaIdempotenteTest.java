package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.workflow.DespachoActivities;
import com.enlace.evaluacion03.workflow.DespachoActivitiesImpl;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.time.Duration;
import java.util.List;

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

        List<ReciboEnvio> recibos = ejecutarEnTemporal(
                activities, "workflow-e05-reintento", "UNIDAD_ANDINA", "UNIDAD_ANDINA");

        assertThat(recibos).hasSize(2);
        assertThat(recibos.get(1)).isEqualTo(recibos.get(0));
        assertThat(activities.efectosRegistrados()).isOne();
    }

    @Test
    void un_comando_explicito_no_puede_reutilizarse_con_otros_datos() {
        requiereDecision();
        assumeFalse(Decisiones.IDEMPOTENCIA != Decisiones.Idempotencia.COMANDO_EXPLICITO,
                "El conflicto de comando explícito no corresponde al camino de clave natural");
        DespachoActivitiesImpl activities = new DespachoActivitiesImpl();

        assertThatThrownBy(() -> ejecutarEnTemporal(
                activities, "workflow-e05-conflicto", "UNIDAD_ANDINA", "UNIDAD_COSTERA"))
                .hasStackTraceContaining("comando");
        assertThat(activities.efectosRegistrados()).isOne();
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.IDEMPOTENCIA == Decisiones.Idempotencia.PENDIENTE,
                "Primero complete DECISION(EV03-E05)");
    }

    private List<ReciboEnvio> ejecutarEnTemporal(
            DespachoActivitiesImpl activities, String workflowId,
            String primeraUnidad, String segundaUnidad) {
        try (TestWorkflowEnvironment environment = TestWorkflowEnvironment.newInstance()) {
            String taskQueue = "ev03-e05-temporal";
            Worker worker = environment.newWorker(taskQueue);
            worker.registerWorkflowImplementationTypes(ReintentoActivityWorkflowImpl.class);
            worker.registerActivitiesImplementations(activities);
            environment.start();

            ReintentoActivityWorkflow workflow = environment.getWorkflowClient().newWorkflowStub(
                    ReintentoActivityWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setTaskQueue(taskQueue)
                            .setWorkflowId(workflowId)
                            .build());
            return workflow.ejecutar(
                    "ORDEN-E05", primeraUnidad, segundaUnidad, "CMD-COMPARTIDO");
        }
    }

    @WorkflowInterface
    public interface ReintentoActivityWorkflow {
        @WorkflowMethod
        List<ReciboEnvio> ejecutar(
                String ordenId, String primeraUnidad, String segundaUnidad, String comandoId);
    }

    public static class ReintentoActivityWorkflowImpl implements ReintentoActivityWorkflow {
        private final DespachoActivities activities = Workflow.newActivityStub(
                DespachoActivities.class,
                ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofSeconds(2))
                        .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
                        .build());

        @Override
        public List<ReciboEnvio> ejecutar(
                String ordenId, String primeraUnidad, String segundaUnidad, String comandoId) {
            ReciboEnvio primero = activities.enviar(ordenId, primeraUnidad, comandoId);
            ReciboEnvio reintento = activities.enviar(ordenId, segundaUnidad, comandoId);
            return List.of(primero, reintento);
        }
    }
}
