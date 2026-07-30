package com.sigeo.clase13;

import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VersionadoWorkflowTest {
    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private VersionadoWorkflow workflow;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("VERSIONADO_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(VersionadoWorkflowImpl.class);
        testEnv.start();
        
        workflow = testEnv.getWorkflowClient().newWorkflowStub(
            VersionadoWorkflow.class,
            io.temporal.client.WorkflowOptions.newBuilder()
                .setTaskQueue("VERSIONADO_TASK_QUEUE")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testVersionado() {
        // Al ejecutar un workflow nuevo, debería usar la versión más reciente (1)
        String resultado = workflow.procesar();
        assertThat(resultado).isEqualTo("Procesado CON validación extra");
    }
}
