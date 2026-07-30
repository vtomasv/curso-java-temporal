package com.sigeo.clase12;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SagaWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("SAGA_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(SagaWorkflowImpl.class);
        worker.registerActivitiesImplementations(new SagaActivitiesImpl());
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testSagaMinima() {
        SagaWorkflow workflow = workflowClient.newWorkflowStub(SagaWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("SAGA_TASK_QUEUE").build());

        String result = workflow.executeSaga("res-1", false, false, false);
        assertThat(result).isEqualTo("COMPLETED");
    }

    @Test
    void testFalloPorEtapa() {
        SagaWorkflow workflow = workflowClient.newWorkflowStub(SagaWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("SAGA_TASK_QUEUE").build());

        assertThatThrownBy(() -> workflow.executeSaga("res-2", true, false, false))
                .hasMessageContaining("Simulated budget allocation failure");
        
        assertThat(workflow.getStatus()).isEqualTo("COMPENSATED");
    }

    @Test
    void testCompensacionInestable() {
        SagaWorkflow workflow = workflowClient.newWorkflowStub(SagaWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("SAGA_TASK_QUEUE").build());

        // TODO: Configure mock to fail compensation temporarily
        assertThatThrownBy(() -> workflow.executeSaga("res-3", true, false, false))
                .hasMessageContaining("Simulated budget allocation failure");
    }

    @Test
    void testChildWorkflows() {
        // Requires registering child workflow implementation
        // worker.registerWorkflowImplementationTypes(ResourceChildWorkflowImpl.class);
        // testEnv.start();
        // ...
    }
}
