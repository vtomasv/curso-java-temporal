package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.workflow.SolicitudActivities;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflow;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflowImpl;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflowInput;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudWorkflowTest {

    private TestWorkflowEnvironment testEnv;

    @AfterEach
    void closeEnvironment() {
        if (testEnv != null) {
            testEnv.close();
        }
    }

    @Test
    void orquestaRegistroYNotificacionSoloMedianteActivities() {
        RecordingActivities activities = new RecordingActivities();
        SolicitudWorkflow workflow = workflowCon(activities);

        var result = workflow.procesar(input("SOL-200"));

        assertThat(result.id()).isEqualTo("REG-SOL-200");
        assertThat(result.estado()).isEqualTo("REGISTRADA");
        assertThat(result.mensaje()).contains("notificada");
        assertThat(activities.registros.get()).isEqualTo(1);
        assertThat(activities.notificaciones.get()).isEqualTo(1);
    }

    @Test
    void reintentaUnFalloTransitorioHastaTresIntentos() {
        RecordingActivities activities = new RecordingActivities();
        activities.fallosTransitoriosRestantes.set(2);
        SolicitudWorkflow workflow = workflowCon(activities);

        var result = workflow.procesar(input("SOL-201"));

        assertThat(result.id()).isEqualTo("REG-SOL-201");
        assertThat(activities.registros.get()).isEqualTo(3);
    }

    @Test
    void noReintentaUnFalloDeValidacion() {
        RecordingActivities activities = new RecordingActivities();
        activities.falloValidacion = true;
        SolicitudWorkflow workflow = workflowCon(activities);

        assertThatThrownBy(() -> workflow.procesar(input("SOL-202")))
                .isInstanceOf(WorkflowException.class)
                .hasRootCauseInstanceOf(ApplicationFailure.class);
        assertThat(activities.registros.get()).isEqualTo(1);
    }

    private SolicitudWorkflow workflowCon(SolicitudActivities activities) {
        testEnv = TestWorkflowEnvironment.newInstance();
        Worker worker = testEnv.newWorker("EV02_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SolicitudWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);
        testEnv.start();

        WorkflowClient client = testEnv.getWorkflowClient();
        return client.newWorkflowStub(
                SolicitudWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("EV02-" + System.nanoTime())
                        .setTaskQueue("EV02_TASK_QUEUE")
                        .build());
    }

    private static SolicitudWorkflowInput input(String id) {
        return new SolicitudWorkflowInput(id, "Reponer equipo", "KEY-" + id);
    }

    private static final class RecordingActivities implements SolicitudActivities {
        private final AtomicInteger registros = new AtomicInteger();
        private final AtomicInteger notificaciones = new AtomicInteger();
        private final AtomicInteger fallosTransitoriosRestantes = new AtomicInteger();
        private boolean falloValidacion;

        @Override
        public String registrar(SolicitudWorkflowInput input) {
            registros.incrementAndGet();
            if (falloValidacion) {
                throw ApplicationFailure.newFailure("Entrada inválida", "VALIDATION");
            }
            if (fallosTransitoriosRestantes.getAndDecrement() > 0) {
                throw ApplicationFailure.newFailure("Proveedor temporalmente no disponible", "TRANSIENT");
            }
            return "REG-" + input.id();
        }

        @Override
        public void notificar(String solicitudId) {
            notificaciones.incrementAndGet();
        }
    }
}

