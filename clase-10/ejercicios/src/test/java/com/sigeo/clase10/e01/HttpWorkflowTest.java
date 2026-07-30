package com.sigeo.clase10.e01;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.TimeoutFailure;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("HttpTaskQueue");
        worker.registerWorkflowImplementationTypes(HttpWorkflowImpl.class);
        worker.registerActivitiesImplementations(new HttpActivityImpl());
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testCallSucceedsWithinTimeout() {
        HttpWorkflow workflow = workflowClient.newWorkflowStub(HttpWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("HttpTaskQueue").build());

        String result = workflow.executeCall(1);
        assertThat(result).isEqualTo("Success");
    }

    @Test
    void testCallFailsWithTimeout() {
        HttpWorkflow workflow = workflowClient.newWorkflowStub(HttpWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("HttpTaskQueue").build());

        assertThatThrownBy(() -> workflow.executeCall(3))
                .isInstanceOf(WorkflowException.class)
                .hasCauseInstanceOf(ActivityFailure.class)
                .hasRootCauseInstanceOf(TimeoutFailure.class);
    }
}
