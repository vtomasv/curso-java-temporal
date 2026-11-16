package com.enlace.evaluacion03;

import com.enlace.evaluacion03.workflow.DespachoActivities;
import com.enlace.evaluacion03.workflow.DespachoInput;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import com.enlace.evaluacion03.workflow.DespachoWorkflowImpl;
import com.enlace.evaluacion03.workflow.EntregaUnidadWorkflowImpl;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import com.enlace.evaluacion03.workflow.FirmaProgreso;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import com.enlace.evaluacion03.workflow.ResultadoDespacho;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "base|all")
class TemporalFirmaConcurrencyTest {

    @Test
    void serializa_updates_concurrentes_y_solicita_el_despacho_una_sola_vez() throws Exception {
        String taskQueue = "ev03-firmas-concurrentes";
        String ordenId = "ORD-FIRMAS-CONCURRENTES";
        String workflowId = "despacho-" + ordenId;
        FirmasConcurrentesActivities activities = new FirmasConcurrentesActivities();

        try (TestWorkflowEnvironment environment = TestWorkflowEnvironment.newInstance()) {
            Worker worker = environment.newWorker(taskQueue);
            worker.registerWorkflowImplementationTypes(
                    DespachoWorkflowImpl.class, EntregaUnidadWorkflowImpl.class);
            worker.registerActivitiesImplementations(activities);
            environment.start();

            DespachoWorkflow workflow = environment.getWorkflowClient().newWorkflowStub(
                    DespachoWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setTaskQueue(taskQueue)
                            .setWorkflowId(workflowId)
                            .build());
            WorkflowClient.start(workflow::iniciar,
                    new DespachoInput(ordenId, List.of("UNIDAD_ANDINA")));

            DespachoWorkflow primerCliente = environment.getWorkflowClient()
                    .newWorkflowStub(DespachoWorkflow.class, workflowId);
            DespachoWorkflow segundoCliente = environment.getWorkflowClient()
                    .newWorkflowStub(DespachoWorkflow.class, workflowId);

            ExecutorService executor = Executors.newFixedThreadPool(2);
            try {
                Future<FirmaProgreso> primera = executor.submit(
                        () -> primerCliente.registrarFirma("firmante-lento", "OFICIAL"));
                activities.esperarPrimeraInstantanea();
                Future<FirmaProgreso> segunda = executor.submit(
                        () -> segundoCliente.registrarFirma("firmante-rapido", "COMANDANTE"));

                assertThat(primera.get(5, TimeUnit.SECONDS).completa()).isFalse();
                assertThat(segunda.get(5, TimeUnit.SECONDS).completa()).isTrue();
            } finally {
                executor.shutdownNow();
            }

            FirmaProgreso finalFirmas = workflow.consultarFirmas();
            assertThat(finalFirmas.completa()).isTrue();
            assertThat(finalFirmas.firmantes())
                    .containsExactlyInAnyOrder("firmante-lento", "firmante-rapido");
            assertThat(activities.iniciosDespacho()).isEqualTo(1);

            ReciboEnvio recibo = esperarRecibo(workflow);
            workflow.confirmarRecepcion(recibo.unidad(), recibo.reciboId());
            ResultadoDespacho resultado = WorkflowStub.fromTyped(workflow)
                    .getResult(ResultadoDespacho.class);
            assertThat(resultado.estado()).isEqualTo(EstadoDespacho.ENTREGADA);
            assertThat(activities.iniciosDespacho()).isEqualTo(1);
        }
    }

    private ReciboEnvio esperarRecibo(DespachoWorkflow workflow) {
        long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < limite) {
            List<ReciboEnvio> recibos = workflow.consultarRecibos();
            if (!recibos.isEmpty()) return recibos.getFirst();
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
        }
        throw new AssertionError("Temporal no inició el despacho tras las firmas concurrentes");
    }

    private static final class FirmasConcurrentesActivities implements DespachoActivities {

        private final Set<String> firmantes = ConcurrentHashMap.newKeySet();
        private final CountDownLatch primeraInstantanea = new CountDownLatch(1);
        private final AtomicInteger iniciosDespacho = new AtomicInteger();

        @Override
        public FirmaProgreso consultarFirmas(String ordenId) {
            return progreso();
        }

        @Override
        public FirmaProgreso registrarFirma(String ordenId, String firmante, String rol) {
            if (!firmantes.add(firmante)) {
                throw new IllegalStateException("El usuario ya firmó la orden");
            }

            FirmaProgreso instantanea = progreso();
            if ("firmante-lento".equals(firmante)) {
                primeraInstantanea.countDown();
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(250));
            }
            return instantanea;
        }

        @Override
        public void iniciarDespacho(String ordenId) {
            iniciosDespacho.incrementAndGet();
        }

        @Override
        public ReciboEnvio enviar(String ordenId, String unidad, String comandoId) {
            return new ReciboEnvio(ordenId, unidad, "REC-" + unidad);
        }

        @Override
        public void registrarEstado(String ordenId, EstadoDespacho estado) {
            // El test observa el resultado devuelto por el Workflow.
        }

        @Override
        public void compensar(String ordenId, String unidad, String reciboId) {
            // No corresponde a esta prueba.
        }

        void esperarPrimeraInstantanea() throws InterruptedException {
            assertThat(primeraInstantanea.await(2, TimeUnit.SECONDS))
                    .as("la primera Activity de firma comenzó")
                    .isTrue();
        }

        int iniciosDespacho() {
            return iniciosDespacho.get();
        }

        private FirmaProgreso progreso() {
            Set<String> instantanea = Set.copyOf(firmantes);
            return new FirmaProgreso(
                    instantanea.size(), 2, instantanea.size() >= 2, instantanea);
        }
    }
}
