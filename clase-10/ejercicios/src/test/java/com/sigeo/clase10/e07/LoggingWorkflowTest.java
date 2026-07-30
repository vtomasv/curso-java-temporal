package com.sigeo.clase10.e07;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggingWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("LoggingTaskQueue");
        worker.registerWorkflowImplementationTypes(LoggingWorkflowImpl.class);
        worker.registerActivitiesImplementations(new LoggingActivityImpl());
        workflowClient = testEnv.getWorkflowClient();
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testLogging() {
        LoggingWorkflow workflow = workflowClient.newWorkflowStub(LoggingWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("LoggingTaskQueue").build());

        // Este test simplemente ejecuta el workflow para verificar que los logs se imprimen
        // sin lanzar excepciones. La verificación de logs suele hacerse manualmente o con
        // herramientas específicas en un entorno real.
        workflow.executeWork("SECRET_DATA");
    }
}
