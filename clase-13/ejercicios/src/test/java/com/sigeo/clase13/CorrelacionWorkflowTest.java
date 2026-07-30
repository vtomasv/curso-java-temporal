package com.sigeo.clase13;

import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorrelacionWorkflowTest {
    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private CorrelacionWorkflow workflow;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("CORRELACION_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(CorrelacionWorkflowImpl.class);
        worker.registerActivitiesImplementations(new CorrelacionActivityImpl());
        testEnv.start();
        
        workflow = testEnv.getWorkflowClient().newWorkflowStub(
            CorrelacionWorkflow.class,
            io.temporal.client.WorkflowOptions.newBuilder()
                .setTaskQueue("CORRELACION_TASK_QUEUE")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testCorrelacion() {
        workflow.procesar("REQ-12345");
        // Verificar visualmente en los logs que el correlationId se imprime correctamente
        // y que no hay logs duplicados si hubiera un replay.
    }
}
