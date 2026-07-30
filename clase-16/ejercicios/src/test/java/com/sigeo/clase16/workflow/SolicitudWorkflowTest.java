package com.sigeo.clase16.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SolicitudWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;
    private SolicitudActivities actividadesMock;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("TEST_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SolicitudWorkflowImpl.class);

        actividadesMock = mock(SolicitudActivities.class);
        worker.registerActivitiesImplementations(actividadesMock);

        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void procesarSolicitud_deberiaActualizarEstadoYLlamarActividades() {
        // Arrange
        SolicitudWorkflow workflow = workflowClient.newWorkflowStub(
                SolicitudWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("TEST_TASK_QUEUE")
                        .build()
        );

        // Act
        workflow.procesarSolicitud(1L);

        // Assert
        verify(actividadesMock).guardarEstadoSolicitud(1L, "PROCESANDO");
        verify(actividadesMock).guardarEstadoSolicitud(1L, "COMPLETADO");
        assertThat(workflow.getEstadoActual()).isEqualTo("COMPLETADO");
    }
}
