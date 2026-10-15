package com.sigeo.evaluacion02;

import com.sigeo.evaluacion02.workflow.SolicitudActivities;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflow;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflowImpl;
import com.sigeo.evaluacion02.workflow.SolicitudWorkflowInput;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestWorkflowEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudWorkflowCasosLimiteTest {

    private TestWorkflowEnvironment testEnv;

    @AfterEach
    void cerrarEntorno() {
        if (testEnv != null) {
            testEnv.close();
        }
    }

    @Test
    void unRegistroQueSiempreFallaAgotaTresIntentosSinNotificar() {
        ActivitiesDePrueba activities = new ActivitiesDePrueba();
        activities.fallarRegistro = true;
        SolicitudWorkflow workflow = workflowCon(activities);

        assertThatThrownBy(() -> workflow.procesar(input()))
                .isInstanceOf(WorkflowException.class)
                .hasRootCauseInstanceOf(ApplicationFailure.class);
        assertThat(activities.registros.get()).isEqualTo(3);
        assertThat(activities.notificaciones.get()).isZero();
    }

    @Test
    void reintentarNotificacionUsaElIdRegistradoSinRepetirElRegistro() {
        ActivitiesDePrueba activities = new ActivitiesDePrueba();
        activities.fallosNotificacionRestantes.set(2);
        SolicitudWorkflow workflow = workflowCon(activities);

        var resultado = workflow.procesar(input());

        assertThat(resultado.id()).isEqualTo("REG-SOL-300");
        assertThat(resultado.estado()).isEqualTo("REGISTRADA");
        assertThat(activities.registros.get()).isEqualTo(1);
        assertThat(activities.notificaciones.get()).isEqualTo(3);
        assertThat(activities.llamadas).containsExactly(
                "registrar:SOL-300",
                "notificar:REG-SOL-300",
                "notificar:REG-SOL-300",
                "notificar:REG-SOL-300");
    }

    private SolicitudWorkflow workflowCon(SolicitudActivities activities) {
        testEnv = TestWorkflowEnvironment.newInstance();
        var worker = testEnv.newWorker("EV02_LIMITES_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SolicitudWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);
        testEnv.start();

        return testEnv.getWorkflowClient().newWorkflowStub(
                SolicitudWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("EV02-LIMITES")
                        .setTaskQueue("EV02_LIMITES_TASK_QUEUE")
                        .build());
    }

    private static SolicitudWorkflowInput input() {
        return new SolicitudWorkflowInput("SOL-300", "Reponer equipo", "KEY-300");
    }

    private static final class ActivitiesDePrueba implements SolicitudActivities {
        private final AtomicInteger registros = new AtomicInteger();
        private final AtomicInteger notificaciones = new AtomicInteger();
        private final AtomicInteger fallosNotificacionRestantes = new AtomicInteger();
        private final ConcurrentLinkedQueue<String> llamadas = new ConcurrentLinkedQueue<>();
        private boolean fallarRegistro;

        @Override
        public String registrar(SolicitudWorkflowInput input) {
            registros.incrementAndGet();
            llamadas.add("registrar:" + input.id());
            if (fallarRegistro) {
                throw ApplicationFailure.newFailure("Proveedor no disponible", "TRANSIENT");
            }
            return "REG-" + input.id();
        }

        @Override
        public void notificar(String solicitudId) {
            notificaciones.incrementAndGet();
            llamadas.add("notificar:" + solicitudId);
            if (fallosNotificacionRestantes.getAndDecrement() > 0) {
                throw ApplicationFailure.newFailure("Notificación no disponible", "TRANSIENT");
            }
        }
    }
}
