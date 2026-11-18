package com.enlace.evaluacion03;

import com.enlace.evaluacion03.config.Decisiones;
import com.enlace.evaluacion03.domain.Confidencialidad;
import com.enlace.evaluacion03.domain.EstadoOrden;
import com.enlace.evaluacion03.domain.FirmaOrden;
import com.enlace.evaluacion03.domain.Orden;
import com.enlace.evaluacion03.domain.TipoOrden;
import com.enlace.evaluacion03.domain.UnidadDestino;
import com.enlace.evaluacion03.service.PoliticaFirmas;
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@EnabledIfSystemProperty(named = "ev03.exercise", matches = "e04|all")
class E04FirmasMultiplesTest {

    @Test
    void e04_exige_elegir_una_politica_de_firmas() {
        assertThat(Decisiones.FIRMAS)
                .as("EV03-E04: cambie FIRMAS y documente roles u origen del umbral")
                .isNotEqualTo(Decisiones.Firmas.PENDIENTE);
    }

    @Test
    void las_firmas_entran_como_updates_y_temporal_habilita_el_despacho() {
        requiereDecision();
        Orden orden = ordenMaxima();
        PoliticaFirmas politica = new PoliticaFirmas();
        FirmasActivities activities = new FirmasActivities(orden, politica);
        List<String> roles = rolesDePrueba(orden, politica);

        try (TestWorkflowEnvironment environment = TestWorkflowEnvironment.newInstance()) {
            Worker worker = environment.newWorker("e04-firmas-temporal");
            worker.registerWorkflowImplementationTypes(
                    DespachoWorkflowImpl.class, EntregaUnidadWorkflowImpl.class);
            worker.registerActivitiesImplementations(activities);
            environment.start();

            DespachoWorkflow workflow = environment.getWorkflowClient().newWorkflowStub(
                    DespachoWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setTaskQueue("e04-firmas-temporal")
                            .setWorkflowId("despacho-" + orden.getId())
                            .build());
            WorkflowClient.start(workflow::iniciar, new DespachoInput(
                    orden.getId(), List.of(UnidadDestino.UNIDAD_ANDINA.name())));

            FirmaProgreso primera = workflow.registrarFirma("persona-1", roles.get(0));
            assertThat(primera.completa()).isFalse();
            assertThat(orden.getEstado()).isEqualTo(EstadoOrden.PENDIENTE_FIRMAS);

            assertThatThrownBy(() -> workflow.registrarFirma("persona-1", roles.get(0)))
                    .rootCause()
                    .hasMessageContaining("ya firmó");

            FirmaProgreso segunda = workflow.registrarFirma("persona-2", roles.get(1));
            assertThat(segunda.completa()).isTrue();
            assertThat(segunda.firmantes()).containsExactlyInAnyOrder("persona-1", "persona-2");
            assertThat(orden.getEstado()).isEqualTo(EstadoOrden.EN_DESPACHO);

            ReciboEnvio recibo = esperarRecibo(workflow);
            workflow.confirmarRecepcion(recibo.unidad(), recibo.reciboId());
            ResultadoDespacho resultado = WorkflowStub.fromTyped(workflow)
                    .getResult(ResultadoDespacho.class);

            assertThat(resultado.estado()).isEqualTo(EstadoDespacho.ENTREGADA);
            assertThat(orden.getEstado()).isEqualTo(EstadoOrden.ENTREGADA);
        }
    }

    private List<String> rolesDePrueba(Orden orden, PoliticaFirmas politica) {
        if (Decisiones.FIRMAS == Decisiones.Firmas.ROLES_FIJOS) {
            assertThat(politica.rolPuedeFirmar(orden, "OFICIAL")).isTrue();
            assertThat(politica.rolPuedeFirmar(orden, "COMANDANTE")).isTrue();
            return List.of("OFICIAL", "COMANDANTE");
        }
        String rol = Stream.of("OFICIAL", "COMANDANTE")
                .filter(candidato -> politica.rolPuedeFirmar(orden, candidato))
                .findFirst()
                .orElseThrow(() -> new AssertionError("La política no habilitó un rol firmante"));
        return List.of(rol, rol);
    }

    private ReciboEnvio esperarRecibo(DespachoWorkflow workflow) {
        long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < limite) {
            List<ReciboEnvio> recibos = workflow.consultarRecibos();
            if (!recibos.isEmpty()) return recibos.getFirst();
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
        }
        throw new AssertionError("Temporal no inició el despacho después de completar las firmas");
    }

    private void requiereDecision() {
        assumeFalse(Decisiones.FIRMAS == Decisiones.Firmas.PENDIENTE,
                "Primero complete DECISION(EV03-E04)");
    }

    private Orden ordenMaxima() {
        return new Orden("ORDEN-E04", "Asunto E04", "Contenido máximo para validar firmas",
                TipoOrden.ORDEN, Confidencialidad.MAXIMA,
                Set.of(UnidadDestino.UNIDAD_ANDINA), "oficial1", "IDEM-E04");
    }

    private static final class FirmasActivities implements DespachoActivities {

        private final Orden orden;
        private final PoliticaFirmas politica;
        private final List<FirmaOrden> firmas = new ArrayList<>();

        private FirmasActivities(Orden orden, PoliticaFirmas politica) {
            this.orden = orden;
            this.politica = politica;
        }

        @Override
        public FirmaProgreso consultarFirmas(String ordenId) {
            return progreso();
        }

        @Override
        public FirmaProgreso registrarFirma(String ordenId, String firmante, String rol) {
            if (!politica.rolPuedeFirmar(orden, rol)) {
                throw new IllegalArgumentException("rol no autorizado");
            }
            if (firmas.stream().anyMatch(firma -> firma.getFirmante().equals(firmante))) {
                throw new IllegalStateException("El usuario ya firmó la orden");
            }
            firmas.add(new FirmaOrden(ordenId, firmante, rol));
            if (politica.requisitoCumplido(orden, firmas)) orden.marcarListaParaEnvio();
            return progreso();
        }

        @Override
        public void iniciarDespacho(String ordenId) {
            orden.marcarEnDespacho();
        }

        @Override
        public ReciboEnvio enviar(String ordenId, String unidad, String comandoId) {
            return new ReciboEnvio(ordenId, unidad, "REC-" + unidad);
        }

        @Override
        public void registrarEstado(String ordenId, EstadoDespacho estado) {
            EstadoOrden destino = switch (estado) {
                case ENTREGADA -> EstadoOrden.ENTREGADA;
                case PENDIENTE_REVISION -> EstadoOrden.PENDIENTE_REVISION;
                case CANCELADA -> EstadoOrden.CANCELADA;
                default -> throw new IllegalArgumentException("estado no terminal");
            };
            orden.marcarResultado(destino);
        }

        @Override
        public void compensar(String ordenId, String unidad, String reciboId) {
            // No corresponde a esta prueba.
        }

        private FirmaProgreso progreso() {
            LinkedHashSet<String> firmantes = firmas.stream()
                    .map(FirmaOrden::getFirmante)
                    .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
            return new FirmaProgreso(
                    firmantes.size(), politica.firmasRequeridas(orden),
                    politica.requisitoCumplido(orden, firmas), firmantes);
        }
    }
}
