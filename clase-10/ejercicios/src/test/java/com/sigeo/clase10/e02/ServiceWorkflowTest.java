package com.sigeo.clase10.e02;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("ServiceTaskQueue");
        worker.registerWorkflowImplementationTypes(ServiceWorkflowImpl.class);
        worker.registerActivitiesImplementations(new ServiceActivityImpl());
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testRetryOn503() {
        ServiceWorkflow workflow = workflowClient.newWorkflowStub(ServiceWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ServiceTaskQueue").build());

        String result = workflow.executeService("503");
        assertThat(result).isEqualTo("Processed: 503");
    }

    @Test
    void testDoNotRetryOn400() {
        ServiceWorkflow workflow = workflowClient.newWorkflowStub(ServiceWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("ServiceTaskQueue").build());

        assertThatThrownBy(() -> workflow.executeService("400"))
                .isInstanceOf(WorkflowException.class)
                .hasCauseInstanceOf(ActivityFailure.class)
                .hasRootCauseInstanceOf(ApplicationFailure.class)
                .satisfies(e -> {
                    ApplicationFailure failure = (ApplicationFailure) e.getCause().getCause();
                    assertThat(failure.getType()).isEqualTo("400");
                });
    }
}
