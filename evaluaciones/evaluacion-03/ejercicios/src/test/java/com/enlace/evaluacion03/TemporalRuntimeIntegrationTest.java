package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.TemporalConfig;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.repository.FirmaOrdenRepository;
import com.enlace.evaluacion03.repository.OrdenRepository;
import com.enlace.evaluacion03.service.DespachoTemporalService;
import com.enlace.evaluacion03.service.OrdenTemporalService;
import com.enlace.evaluacion03.service.OrdenService;
import com.enlace.evaluacion03.service.PoliticaFirmas;
import com.enlace.evaluacion03.workflow.AccesoProgreso;
import com.enlace.evaluacion03.workflow.AccesoWorkflowImpl;
import com.enlace.evaluacion03.workflow.DespachoActivitiesImpl;
import com.enlace.evaluacion03.workflow.DespachoWorkflow;
import com.enlace.evaluacion03.workflow.DespachoWorkflowImpl;
import com.enlace.evaluacion03.workflow.EntregaUnidadWorkflowImpl;
import com.enlace.evaluacion03.workflow.EstadoDespacho;
import com.enlace.evaluacion03.workflow.FirmaProgreso;
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
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(properties = "sicor.temporal.enabled=false")
@AutoConfigureMockMvc
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

    @Autowired
    private FirmaOrdenRepository firmaRepository;

    @Autowired
    private PoliticaFirmas politicaFirmas;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void la_pagina_de_ingreso_personalizada_renderiza_sin_autenticacion() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("Ingresar al centro de operaciones")))
                .andExpect(content().string(containsString("comandante2")));
    }

    @Test
    @WithMockUser(username = "operador", roles = "OPERADOR")
    void el_operador_recibe_un_dashboard_renderizado_y_puede_crear() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Bandeja operacional")))
                .andExpect(content().string(containsString("Preparar orden")));
    }

    @Test
    @WithMockUser(username = "oficial1", roles = "OFICIAL")
    void el_oficial_no_ve_un_formulario_que_su_rol_no_puede_enviar() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("separación de funciones")))
                .andExpect(content().string(not(containsString("Preparar orden"))));
    }

    @Test
    @WithMockUser(username = "operador", roles = "OPERADOR")
    void el_detalle_interno_renderiza_la_evidencia_temporal() throws Exception {
        mockMvc.perform(get("/ordenes/ORD-DEMO-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("detalle"))
                .andExpect(content().string(containsString("Evidencia durable")))
                .andExpect(content().string(containsString("despacho-ORD-DEMO-01")));
    }

    @Test
    @WithMockUser(username = "auditor", roles = "AUDITOR")
    void la_reautenticacion_maxima_no_filtra_el_contenido_en_la_respuesta_previa() throws Exception {
        mockMvc.perform(get("/ordenes/ORD-DEMO-02"))
                .andExpect(status().isOk())
                .andExpect(view().name("reauth"))
                .andExpect(content().string(containsString("Confirma tu identidad")))
                .andExpect(content().string(not(containsString(
                        "Contenido ficticio de demostración para validar la reautenticación máxima."))));
    }

    @Test
    void la_aplicacion_procesa_firmas_por_update_antes_de_query_signal_y_activity() {
        String ordenId = "ORD-RUNTIME-SIGNAL";
        guardarPendienteFirmas(ordenId);

        temporal.iniciar(ordenes.prepararWorkflow(ordenId));
        AccesoProgreso acceso = temporal.registrarAcceso(
                ordenes.prepararWorkflow(ordenId), "ACCESO-" + ordenId, "auditor", true);
        FirmaProgreso firmas = autorizarMedianteTemporal(ordenId);

        assertThat(acceso.accesosAutorizados()).isOne();
        assertThat(acceso.reautenticaciones()).isOne();
        assertThat(temporal.consultarAccesos(ordenId)).isEqualTo(acceso);
        assertThat(firmas.completa()).isTrue();
        assertThat(firmaRepository.countByOrdenId(ordenId)).isEqualTo(firmas.registradas());
        assertThat(repository.findById(ordenId).orElseThrow().getEstado())
                .isEqualTo(EstadoOrden.EN_DESPACHO);

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

        AccesoProgreso accesoPosterior = temporal.registrarAcceso(
                ordenes.prepararWorkflow(ordenId),
                "ACCESO-POSTERIOR-" + ordenId,
                "auditor",
                false);
        assertThat(accesoPosterior.accesosAutorizados())
                .as("la auditoría permanece disponible después de terminar el despacho")
                .isEqualTo(2);
        assertThat(accesoPosterior.reautenticaciones()).isOne();
        assertThat(temporal.consultarAccesos(ordenId)).isEqualTo(accesoPosterior);
    }

    @Test
    void la_aplicacion_envia_un_update_y_persiste_la_cancelacion() {
        String ordenId = "ORD-RUNTIME-UPDATE";
        guardarPendienteFirmas(ordenId);

        temporal.iniciar(ordenes.prepararWorkflow(ordenId));
        assertThat(autorizarMedianteTemporal(ordenId).completa()).isTrue();
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

    private FirmaProgreso autorizarMedianteTemporal(String ordenId) {
        Orden orden = repository.findById(ordenId).orElseThrow();
        int requeridas = politicaFirmas.firmasRequeridas(orden);
        if (requeridas < 1 || requeridas > 10) {
            throw new AssertionError("Cantidad de firmas no implementada: " + requeridas);
        }
        List<String> permitidos = List.of("OFICIAL", "COMANDANTE", "AUDITOR").stream()
                .filter(rol -> politicaFirmas.rolPuedeFirmar(orden, rol))
                .toList();
        if (permitidos.isEmpty()) throw new AssertionError("La política no admite roles de prueba");

        FirmaProgreso progreso = temporal.consultarFirmas(ordenId);
        for (int i = 0; i < requeridas && !progreso.completa(); i++) {
            String rol = permitidos.get(i % permitidos.size());
            progreso = temporal.registrarFirma(
                    ordenes.prepararWorkflow(ordenId), "firmante-runtime-" + i, rol);
        }
        return progreso;
    }

    private void guardarPendienteFirmas(String ordenId) {
        Orden orden = new Orden(
                ordenId,
                "Verificación de integración Temporal",
                "Contenido ficticio suficiente para verificar el despacho durable.",
                TipoOrden.ORDEN,
                Confidencialidad.MAXIMA,
                Set.of(UnidadDestino.UNIDAD_ANDINA),
                "operador",
                "IDEM-" + ordenId
        );
        repository.saveAndFlush(orden);
    }

    /**
     * El motor embebido existe exclusivamente en test. La aplicación que usa el
     * estudiante siempre se conecta al servidor Temporal externo configurado.
     */
    @TestConfiguration(proxyBeanMethods = false)
    static class TemporalTestConfig {

        @Bean
        DespachoActivitiesImpl despachoActivities(OrdenTemporalService ordenes) {
            return new DespachoActivitiesImpl(new DespachoActivitiesImpl.OrdenLifecycle() {
                @Override
                public FirmaProgreso consultarFirmas(String ordenId) {
                    return ordenes.consultarFirmas(ordenId);
                }

                @Override
                public FirmaProgreso registrarFirma(String ordenId, String firmante, String rol) {
                    return ordenes.registrarFirma(ordenId, firmante, rol);
                }

                @Override
                public void iniciarDespacho(String ordenId) {
                    ordenes.iniciarDespacho(ordenId);
                }

                @Override
                public void registrarEstado(String ordenId, EstadoDespacho estado) {
                    ordenes.registrarEstado(ordenId, estado);
                }
            });
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
                    AccesoWorkflowImpl.class,
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
