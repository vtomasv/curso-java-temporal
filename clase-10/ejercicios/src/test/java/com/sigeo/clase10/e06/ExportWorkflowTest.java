package com.sigeo.clase10.e06;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExportWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;
    private ExportActivityImpl activityImpl;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("ExportTaskQueue");
        worker.registerWorkflowImplementationTypes(ExportWorkflowImpl.class);
        activityImpl = new ExportActivityImpl();
        worker.registerActivitiesImplementations(activityImpl);
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testCancellation() throws InterruptedException {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("ExportTaskQueue")
                .build();
        
        ExportWorkflow workflow = workflowClient.newWorkflowStub(ExportWorkflow.class, options);
        
        // Iniciar asíncronamente
        CompletableFuture<Void> future = WorkflowClient.execute(workflow::startExport);
        
        // Esperar un poco para que la actividad comience
        Thread.sleep(500);
        
        // Cancelar el workflow
        WorkflowStub stub = WorkflowStub.fromTyped(workflow);
        stub.cancel();
        
        // Esperar a que termine (debería lanzar excepción de cancelación)
        assertThatThrownBy(future::get)
                .isInstanceOf(ExecutionException.class);
                
        // Verificar que se llamó al cleanup
        assertThat(activityImpl.isCleanupCalled()).isTrue();
    }
}
