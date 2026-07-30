package com.sigeo.clase14;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnalisisWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;
    private AnalisisAiActivity mockActivity;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("AnalisisTaskQueue");
        worker.registerWorkflowImplementationTypes(AnalisisWorkflowImpl.class);

        mockActivity = mock(AnalisisAiActivity.class);
        worker.registerActivitiesImplementations(mockActivity);

        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testEjecutarAnalisis() {
        // TODO(C14-E06): Configurar mock de activity y verificar que el workflow devuelve el resultado esperado
        when(mockActivity.analizarTexto(any())).thenReturn(new AnalisisAiActivity.AnalisisResponse("Resultado OK", "gpt-4o-mini"));

        AnalisisWorkflow workflow = workflowClient.newWorkflowStub(AnalisisWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("AnalisisTaskQueue").build());

        String resultado = null;
        try {
            resultado = workflow.ejecutarAnalisis("Texto de prueba");
        } catch (Exception e) {
            // Ignorar
        }

        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo("Resultado OK");
    }
}
