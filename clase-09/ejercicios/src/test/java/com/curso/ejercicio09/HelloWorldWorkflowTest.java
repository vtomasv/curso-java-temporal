package com.curso.ejercicio09;

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
        
        // TODO(ejercicio-3): Descomenta la siguiente línea cuando hayas implementado la clase
        // worker.registerWorkflowImplementationTypes(HelloWorldWorkflowImpl.class);

        testEnv.start();

        // Creamos el stub del workflow para llamarlo en el test
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
        // TODO(ejercicio-4): Ejecuta el workflow pasándole "Mundo" y verifica que el resultado sea "Hola Mundo desde Temporal!"
        // String result = workflow.getGreeting("Mundo");
        // assertEquals("Hola Mundo desde Temporal!", result);
    }
}
