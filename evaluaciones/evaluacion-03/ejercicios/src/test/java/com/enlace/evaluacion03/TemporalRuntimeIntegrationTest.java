package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.TemporalConfig;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.repository.OrdenRepository;
import com.enlace.evaluacion03.service.DespachoTemporalService;
import com.enlace.evaluacion03.service.EstadoOrdenTemporalService;
import com.enlace.evaluacion03.service.OrdenService;
import com.enlace.evaluacion03.workflow.DespachoActivitiesImpl;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import com.enlace.evaluacion03.workflow.DespachoWorkflowImpl;
import com.enlace.evaluacion03.workflow.EntregaUnidadWorkflowImpl;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import com.enlace.evaluacion03.workflow.ReciboEnvio;
import com.enlace.evaluacion03.workflow.ResultadoDespacho;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "sicor.temporal.enabled=false")
@Import(TemporalRuntimeIntegrationTest.TemporalTestConfig.class)
@EnabledIfSystemProperty(named = "ev03.exercise", matches = "base|all")
class TemporalRuntimeIntegrationTest {

    @Autowired
    private WorkflowClient workflowClient;

    @Autowired
    private DespachoTemporalService temporal;

    @Autowired
    private OrdenService ordenes;

    @Autowired
    private OrdenRepository repository;

    @Test
    void la_aplicacion_inicia_un_workflow_real_y_procesa_query_signal_y_activity() {
        String ordenId = "ORD-RUNTIME-SIGNAL";
        guardarListaParaEnvio(ordenId);

        temporal.iniciar(ordenes.prepararDespacho(ordenId));

        var recibos = esperarRecibos(ordenId);
        assertThat(recibos).hasSize(1);
        assertThat(recibos.getFirst().unidad())
                .isEqualTo(UnidadDestino.UNIDAD_ANDINA.name());
        assertThat(temporal.consultarProgreso(ordenId).enviadas()).isOne();

        ReciboEnvio recibo = recibos.getFirst();
        temporal.confirmarRecepcion(ordenId, recibo.unidad(), recibo.reciboId());

        ResultadoDespacho resultado = esperarResultado(ordenId);
        assertThat(resultado.estado()).isEqualTo(EstadoDespacho.ENTREGADA);
        assertThat(resultado.confirmadas()).containsExactly(UnidadDestino.UNIDAD_ANDINA.name());
        assertThat(repository.findById(ordenId).orElseThrow().getEstado())
                .isEqualTo(EstadoOrden.ENTREGADA);
    }

    @Test
    void la_aplicacion_envia_un_update_y_persiste_la_cancelacion() {
        String ordenId = "ORD-RUNTIME-UPDATE";
        guardarListaParaEnvio(ordenId);

        temporal.iniciar(ordenes.prepararDespacho(ordenId));
        assertThat(esperarRecibos(ordenId)).hasSize(1);

        assertThat(temporal.cancelar(ordenId, "Cambio de prioridad de prueba")).isTrue();

        ResultadoDespacho resultado = esperarResultado(ordenId);
        assertThat(resultado.estado()).isEqualTo(EstadoDespacho.CANCELADA);
        assertThat(repository.findById(ordenId).orElseThrow().getEstado())
                .isEqualTo(EstadoOrden.CANCELADA);
    }

    private ResultadoDespacho esperarResultado(String ordenId) {
        DespachoWorkflow workflow = workflowClient.newWorkflowStub(
                DespachoWorkflow.class, temporal.workflowId(ordenId));
        return WorkflowStub.fromTyped(workflow).getResult(ResultadoDespacho.class);
    }

    private java.util.List<ReciboEnvio> esperarRecibos(String ordenId) {
        long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < limite) {
            var recibos = temporal.consultarRecibos(ordenId);
            if (!recibos.isEmpty()) return recibos;
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
        }
        throw new AssertionError("El Worker no produjo recibos para " + ordenId);
    }

    private void guardarListaParaEnvio(String ordenId) {
        Orden orden = new Orden(
                ordenId,
                "Verificación de integración Temporal",
                "Contenido ficticio suficiente para verificar el despacho durable.",
                TipoOrden.INFORMATIVA,
                Confidencialidad.INTERNA,
                Set.of(UnidadDestino.UNIDAD_ANDINA),
                "operador",
                "IDEM-" + ordenId
        );
        orden.marcarListaParaEnvio();
        repository.saveAndFlush(orden);
    }

    /**
     * El motor embebido existe exclusivamente en test. La aplicación que usa el
     * estudiante siempre se conecta al servidor Temporal externo configurado.
     */
    @TestConfiguration(proxyBeanMethods = false)
    static class TemporalTestConfig {

        @Bean
        DespachoActivitiesImpl despachoActivities(EstadoOrdenTemporalService estados) {
            return new DespachoActivitiesImpl(estados::registrar);
        }

        @Bean(destroyMethod = "close")
        TestMotorTemporal testMotorTemporal(DespachoActivitiesImpl activities) {
            return new TestMotorTemporal(activities);
        }

        @Bean
        WorkflowClient workflowClient(TestMotorTemporal motor) {
            return motor.client;
        }
    }

    static final class TestMotorTemporal implements AutoCloseable {

        private final TestWorkflowEnvironment environment;
        private final WorkflowClient client;

        TestMotorTemporal(DespachoActivitiesImpl activities) {
            environment = TestWorkflowEnvironment.newInstance();
            Worker worker = environment.newWorker(TemporalConfig.TASK_QUEUE);
            worker.registerWorkflowImplementationTypes(
                    DespachoWorkflowImpl.class,
                    EntregaUnidadWorkflowImpl.class
            );
            worker.registerActivitiesImplementations(activities);
            client = environment.getWorkflowClient();
            environment.start();
        }

        @Override
        public void close() {
            environment.close();
        }
    }
}
