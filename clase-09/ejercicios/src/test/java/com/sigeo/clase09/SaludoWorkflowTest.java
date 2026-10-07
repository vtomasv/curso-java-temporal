package com.sigeo.clase09;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SaludoWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private SaludoWorkflow workflow;
    private RecordingAuditoriaActivity auditoriaActivity;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("SALUDO_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SaludoWorkflowImpl.class);

        auditoriaActivity = new RecordingAuditoriaActivity();
        worker.registerActivitiesImplementations(auditoriaActivity);

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
        assertThat(auditoriaActivity.mensaje).isEqualTo("Se saludó a: Mundo");
    }

    private static final class RecordingAuditoriaActivity implements AuditoriaActivity {

        private String mensaje;

        @Override
        public void registrarAuditoria(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
