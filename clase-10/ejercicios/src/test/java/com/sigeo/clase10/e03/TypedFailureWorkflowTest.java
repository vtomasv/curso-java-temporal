package com.sigeo.clase10.e03;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TypedFailureWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("TypedFailureTaskQueue");
        worker.registerWorkflowImplementationTypes(TypedFailureWorkflowImpl.class);
        worker.registerActivitiesImplementations(new TypedFailureActivityImpl());
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testValidationError() {
        TypedFailureWorkflow workflow = workflowClient.newWorkflowStub(TypedFailureWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("TypedFailureTaskQueue").build());

        String result = workflow.process("invalid");
        assertThat(result).isEqualTo("Validation Error");
    }

    @Test
    void testNotFoundError() {
        TypedFailureWorkflow workflow = workflowClient.newWorkflowStub(TypedFailureWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("TypedFailureTaskQueue").build());

        String result = workflow.process("missing");
        assertThat(result).isEqualTo("Not Found Error");
    }

    @Test
    void testProviderError() {
        TypedFailureWorkflow workflow = workflowClient.newWorkflowStub(TypedFailureWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("TypedFailureTaskQueue").build());

        String result = workflow.process("down");
        assertThat(result).isEqualTo("Provider Error");
    }
}
