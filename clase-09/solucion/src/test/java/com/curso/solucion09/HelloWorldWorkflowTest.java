package com.curso.solucion09;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private HelloWorldWorkflow workflow;

    @BeforeEach
    public void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("HelloWorldTaskQueue");
        worker.registerWorkflowImplementationTypes(HelloWorldWorkflowImpl.class);
        testEnv.start();

        workflow = testEnv.getWorkflowClient().newWorkflowStub(
                HelloWorldWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue("HelloWorldTaskQueue").build()
        );
    }

    @AfterEach
    public void tearDown() {
        testEnv.close();
    }

    @Test
    public void testGetGreeting() {
        String result = workflow.getGreeting("Mundo");
        assertEquals("Hola Mundo desde Temporal!", result);
    }
}
