package com.sigeo.clase09;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeterminismoWorkflowTest {

    @Test
    void debeEjecutarSoloConApisDeterministas() {
        try (TestWorkflowEnvironment testEnv = TestWorkflowEnvironment.newInstance()) {
            Worker worker = testEnv.newWorker("DETERMINISMO_TASK_QUEUE");
            worker.registerWorkflowImplementationTypes(NoDeterministaWorkflowImpl.class);
            testEnv.start();

            ProcesoDeterministaWorkflow workflow = testEnv.getWorkflowClient().newWorkflowStub(
                    ProcesoDeterministaWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setTaskQueue("DETERMINISMO_TASK_QUEUE")
                            .build()
            );

            assertThat(workflow.ejecutarProceso()).isEqualTo("Completado");
        }
    }
}
