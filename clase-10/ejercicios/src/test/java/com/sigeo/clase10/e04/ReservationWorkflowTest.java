package com.sigeo.clase10.e04;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;
    private ReservationActivityImpl activityImpl;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("ReservationTaskQueue");
        worker.registerWorkflowImplementationTypes(ReservationWorkflowImpl.class);
        activityImpl = new ReservationActivityImpl();
        worker.registerActivitiesImplementations(activityImpl);
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testIdempotentReservation() {
        ReservationWorkflow workflow = workflowClient.newWorkflowStub(ReservationWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("WF-123")
                        .setTaskQueue("ReservationTaskQueue")
                        .build());

        String result = workflow.processReservation("ITEM-1");
        
        assertThat(result).isEqualTo("RES-ITEM-1");
        // La actividad fue llamada dos veces, pero solo debió procesar la reserva una vez
        // (la segunda vez retornó el valor cacheado)
        assertThat(activityImpl.getCallCount()).isEqualTo(2);
    }
}
