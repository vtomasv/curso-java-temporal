package com.sigeo.clase13;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InteraccionWorkflowTest {
    private TestWorkflowEnvironment testEnv;
    private Worker worker;
    private WorkflowClient workflowClient;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        worker = testEnv.newWorker("INTERACCION_TASK_QUEUE");
        worker.registerWorkflowImplementationTypes(InteraccionWorkflowImpl.class);
        testEnv.start();
        workflowClient = testEnv.getWorkflowClient();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testSignalsAndUpdates() throws Exception {
        InteraccionWorkflow workflow = workflowClient.newWorkflowStub(
            InteraccionWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("INTERACCION_TASK_QUEUE")
                .build()
        );

        // Iniciar el workflow asíncronamente
        CompletableFuture<List<String>> resultFuture = WorkflowClient.execute(workflow::ejecutar);

        // Enviar señales
        workflow.agregarEvento("Evento 1");
        workflow.agregarEvento("Evento 2");

        // Consultar estado
        List<String> eventosParciales = workflow.consultarEventos();
        assertThat(eventosParciales).containsExactly("Evento 1", "Evento 2");

        // Enviar update válido
        String updateResult = workflow.actualizarEstado("PROCESANDO");
        assertThat(updateResult).isEqualTo("OK");

        // Enviar update inválido
        assertThatThrownBy(() -> workflow.actualizarEstado(""))
            .hasMessageContaining("Estado inválido");

        // Completar workflow
        workflow.completar();

        // Verificar resultado final
        List<String> resultadoFinal = resultFuture.get();
        assertThat(resultadoFinal).containsExactly(
            "Evento 1", 
            "Evento 2", 
            "Estado actualizado a: PROCESANDO"
        );
    }
}
