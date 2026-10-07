package com.sigeo.clase09;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

class AprobacionWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private AprobacionWorkflow workflow;
    private RecordingAprobacionActivity activity;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("APROBACION_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(AprobacionWorkflowImpl.class);

        activity = new RecordingAprobacionActivity();
        worker.registerActivitiesImplementations(activity);

        testEnv.start();

        workflow = testEnv.getWorkflowClient().newWorkflowStub(
                AprobacionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("APROBACION_TASK_QUEUE")
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void debeAprobarSolicitud() {
        // Ejecutar asíncronamente
        CompletableFuture<String> result = WorkflowClient.execute(workflow::solicitarAprobacion, "REQ-123");
        
        // Enviar señal
        workflow.recibirDecision(true);
        
        // Verificar resultado
        assertThat(result.join()).isEqualTo("APROBADA");
        assertThat(activity.idSolicitud).isEqualTo("REQ-123");
        assertThat(activity.resultado).isEqualTo("APROBADA");
    }

    @Test
    void debeRechazarSolicitud() {
        CompletableFuture<String> result = WorkflowClient.execute(workflow::solicitarAprobacion, "REQ-124");
        
        workflow.recibirDecision(false);
        
        assertThat(result.join()).isEqualTo("RECHAZADA");
        assertThat(activity.idSolicitud).isEqualTo("REQ-124");
        assertThat(activity.resultado).isEqualTo("RECHAZADA");
    }

    @Test
    void debeVencerSolicitud() {
        CompletableFuture<String> result = WorkflowClient.execute(workflow::solicitarAprobacion, "REQ-125");
        
        // Avanzar el tiempo más de 7 días
        testEnv.sleep(Duration.ofDays(8));
        
        assertThat(result.join()).isEqualTo("VENCIDA");
        assertThat(activity.idSolicitud).isEqualTo("REQ-125");
        assertThat(activity.resultado).isEqualTo("VENCIDA");
    }

    private static final class RecordingAprobacionActivity implements AprobacionActivity {

        private String idSolicitud;
        private String resultado;

        @Override
        public void notificarResultado(String idSolicitud, String resultado) {
            this.idSolicitud = idSolicitud;
            this.resultado = resultado;
        }
    }
}
