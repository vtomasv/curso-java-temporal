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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AprobacionWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private AprobacionWorkflow workflow;
    private AprobacionActivity activityMock;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("APROBACION_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(AprobacionWorkflowImpl.class);

        activityMock = mock(AprobacionActivity.class);
        worker.registerActivitiesImplementations(activityMock);

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
        verify(activityMock).notificarResultado("REQ-123", "APROBADA");
    }

    @Test
    void debeRechazarSolicitud() {
        CompletableFuture<String> result = WorkflowClient.execute(workflow::solicitarAprobacion, "REQ-124");
        
        workflow.recibirDecision(false);
        
        assertThat(result.join()).isEqualTo("RECHAZADA");
        verify(activityMock).notificarResultado("REQ-124", "RECHAZADA");
    }

    @Test
    void debeVencerSolicitud() {
        CompletableFuture<String> result = WorkflowClient.execute(workflow::solicitarAprobacion, "REQ-125");
        
        // Avanzar el tiempo más de 7 días
        testEnv.sleep(Duration.ofDays(8));
        
        assertThat(result.join()).isEqualTo("VENCIDA");
        verify(activityMock).notificarResultado("REQ-125", "VENCIDA");
    }
}
