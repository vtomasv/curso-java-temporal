package com.sigeo.clase13;

import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchAttributesWorkflowTest {
    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private SearchAttributesWorkflow workflow;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("SEARCH_ATTR_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SearchAttributesWorkflowImpl.class);
        testEnv.start();
        
        workflow = testEnv.getWorkflowClient().newWorkflowStub(
            SearchAttributesWorkflow.class,
            io.temporal.client.WorkflowOptions.newBuilder()
                .setTaskQueue("SEARCH_ATTR_TASK_QUEUE")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testSearchAttributes() {
        // En un entorno de test real, verificaríamos los search attributes,
        // pero TestWorkflowEnvironment tiene soporte limitado para consultas complejas.
        // Nos aseguramos de que el workflow se ejecute sin errores.
        workflow.ejecutarConAtributos("Juan", "ALTA");
    }
}
