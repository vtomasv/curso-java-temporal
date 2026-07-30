package com.sigeo.clase10.e05;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BatchProcessingWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("BatchTaskQueue");
        worker.registerWorkflowImplementationTypes(BatchProcessingWorkflowImpl.class);
        worker.registerActivitiesImplementations(new BatchProcessingActivityImpl());
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testBatchProcessingWithResume() {
        BatchProcessingWorkflow workflow = workflowClient.newWorkflowStub(BatchProcessingWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("BatchTaskQueue").build());

        // La actividad fallará a los 500, pero gracias al heartbeat y los reintentos automáticos,
        // debería reanudar desde 500 y terminar los 1000.
        int result = workflow.executeBatch(1000);
        
        assertThat(result).isEqualTo(1000);
    }
}
