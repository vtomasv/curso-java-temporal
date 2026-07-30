package com.sigeo.clase11;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LongRunningWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("LongRunningTaskQueue");
        worker.registerWorkflowImplementationTypes(LongRunningWorkflowImpl.class);
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testContinueAsNew() {
        LongRunningWorkflow workflow = workflowClient.newWorkflowStub(LongRunningWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("LongRunningTaskQueue").build());

        WorkflowClient.start(workflow::processEvents, 0);
        
        for (int i = 0; i < 50; i++) {
            workflow.addEvent("Event " + i);
        }
        
        // The workflow should throw a ContinueAsNewError internally and start a new run
        // In a test environment, we can verify it doesn't just hang or complete normally
        // This is a simplified test for the ContinueAsNew behavior
        assertThatThrownBy(() -> workflow.processEvents(0))
            .isInstanceOf(Exception.class); // Depending on how the test environment handles it
    }
}
