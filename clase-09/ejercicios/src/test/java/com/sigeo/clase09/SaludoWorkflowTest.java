package com.sigeo.clase09;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SaludoWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private SaludoWorkflow workflow;
    private AuditoriaActivity auditoriaActivityMock;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("SALUDO_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SaludoWorkflowImpl.class);

        auditoriaActivityMock = mock(AuditoriaActivity.class);
        worker.registerActivitiesImplementations(auditoriaActivityMock);

        testEnv.start();

        workflow = testEnv.getWorkflowClient().newWorkflowStub(
                SaludoWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("SALUDO_TASK_QUEUE")
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void debeSaludarYRegistrarAuditoria() {
        String resultado = workflow.saludar("Mundo");

        assertThat(resultado).isEqualTo("Hola, Mundo");
        verify(auditoriaActivityMock).registrarAuditoria("Se saludó a: Mundo");
    }
}
