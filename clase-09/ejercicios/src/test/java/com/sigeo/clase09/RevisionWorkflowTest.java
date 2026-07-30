package com.sigeo.clase09;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RevisionWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private RevisionWorkflow workflow;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("REVISION_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(RevisionWorkflowImpl.class);
        testEnv.start();

        workflow = testEnv.getWorkflowClient().newWorkflowStub(
                RevisionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("REVISION_TASK_QUEUE")
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void debeEsperarYCompletarRevision() {
        // Iniciar el workflow de forma asíncrona o usar el testEnv para avanzar el tiempo
        // Temporal testing environment permite avanzar el tiempo sin esperar realmente
        
        // Ejecutamos el workflow
        String resultado = workflow.iniciarRevision(5);
        
        assertThat(resultado).isEqualTo("Revisión completada después de 5 días");
    }
}
