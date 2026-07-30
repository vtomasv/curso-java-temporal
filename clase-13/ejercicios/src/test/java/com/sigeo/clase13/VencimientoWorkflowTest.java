package com.sigeo.clase13;

import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VencimientoWorkflowTest {
    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private VencimientoWorkflow workflow;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("VENCIMIENTO_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(VencimientoWorkflowImpl.class);
        testEnv.start();
        
        workflow = testEnv.getWorkflowClient().newWorkflowStub(
            VencimientoWorkflow.class,
            io.temporal.client.WorkflowOptions.newBuilder()
                .setTaskQueue("VENCIMIENTO_TASK_QUEUE")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testVencimientoTimeSkipping() {
        long startTime = System.currentTimeMillis();
        
        String resultado = workflow.procesarConVencimiento(30);
        
        long duration = System.currentTimeMillis() - startTime;
        
        assertThat(resultado).isEqualTo("Vencido tras 30 días");
        // El test debe durar menos de 5 segundos a pesar de esperar 30 días lógicos
        assertThat(duration).isLessThan(5000);
    }
}
