package com.enlace.evaluacion03.workflow;

import com.enlace.evaluacion03.config.Decisiones;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DespachoWorkflowImpl implements DespachoWorkflow {

    private final DespachoActivities activities = Workflow.newActivityStub(
            DespachoActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    .setScheduleToCloseTimeout(Duration.ofSeconds(20))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(3)
                            .setInitialInterval(Duration.ofMillis(100))
                            .setBackoffCoefficient(2.0)
                            .setDoNotRetry("VALIDATION")
                            .build())
                    .build()
    );

    private final List<ReciboEnvio> recibos = new ArrayList<>();
    private final Set<String> confirmadas = new LinkedHashSet<>();
    private EstadoDespacho estado = EstadoDespacho.PREPARANDO;
    private int total;
    private boolean cancelada;

    @Override
    public ResultadoDespacho iniciar(DespachoInput input) {
        total = input.unidades().size();
        estado = EstadoDespacho.ENVIANDO;

        recibos.addAll(switch (Decisiones.ORQUESTACION) {
            case PENDIENTE -> enviarBaseSecuencial(input);
            case SECUENCIAL -> enviarSecuencialElegido(input);
            case PARALELA -> enviarParaleloElegido(input);
            case CHILD_WORKFLOW_POR_UNIDAD -> enviarConHijosElegido(input);
        });

        if (cancelada) return terminar(input.ordenId(), EstadoDespacho.CANCELADA);
        estado = EstadoDespacho.ESPERANDO_CONFIRMACIONES;

        boolean completo = Workflow.await(Duration.ofHours(12), () -> cancelada || alcanzaConfirmacion());
        if (cancelada) return terminar(input.ordenId(), EstadoDespacho.CANCELADA);
        if (completo && alcanzaConfirmacion()) return terminar(input.ordenId(), EstadoDespacho.ENTREGADA);

        tratarIncertidumbre(input.ordenId());
        return terminar(input.ordenId(), EstadoDespacho.PENDIENTE_REVISION);
    }

    private List<ReciboEnvio> enviarBaseSecuencial(DespachoInput input) {
        return input.unidades().stream()
                .map(unidad -> activities.enviar(input.ordenId(), unidad, comando(input.ordenId(), unidad)))
                .toList();
    }

    private List<ReciboEnvio> enviarSecuencialElegido(DespachoInput input) {
        // TODO(EV03-E06-A): implemente y preserve una clave estable por unidad.
        throw new UnsupportedOperationException("TODO EV03-E06-A");
    }

    private List<ReciboEnvio> enviarParaleloElegido(DespachoInput input) {
        // TODO(EV03-E06-B): use Async/Promise; no use CompletableFuture ni threads Java.
        List<Promise<ReciboEnvio>> pendientes = input.unidades().stream()
                .map(unidad -> Async.function(
                        () -> activities.enviar(input.ordenId(), unidad, comando(input.ordenId(), unidad))))
                .toList();
        throw new UnsupportedOperationException("TODO EV03-E06-B: resolver promises " + pendientes.size());
    }

    private List<ReciboEnvio> enviarConHijosElegido(DespachoInput input) {
        // TODO(EV03-E06-C): cree un Child Workflow por unidad con Workflow ID estable.
        input.unidades().forEach(unidad -> Workflow.newChildWorkflowStub(
                EntregaUnidadWorkflow.class,
                ChildWorkflowOptions.newBuilder()
                        .setWorkflowId("entrega-" + input.ordenId() + "-" + unidad)
                        .build()
        ));
        throw new UnsupportedOperationException("TODO EV03-E06-C");
    }

    private boolean alcanzaConfirmacion() {
        return switch (Decisiones.CONFIRMACION) {
            case PENDIENTE -> confirmadas.size() == total;
            case TODAS_LAS_UNIDADES -> {
                // TODO(EV03-E07-A): defina la condición sin depender de orden de llegada.
                yield false;
            }
            case QUORUM_CONFIGURABLE -> {
                // TODO(EV03-E07-B): documente el cálculo y el mínimo permitido.
                yield false;
            }
        };
    }

    private void tratarIncertidumbre(String ordenId) {
        switch (Decisiones.INCERTIDUMBRE) {
            case PENDIENTE, REVISION_MANUAL -> {
                // terminar(...) registra PENDIENTE_REVISION exactamente una vez.
            }
            case COMPENSAR_SOLO_CONFIRMADOS -> {
                // TODO(EV03-E07-C): compense únicamente recibos cuyo resultado sea conocido.
                throw new UnsupportedOperationException("TODO EV03-E07-C");
            }
        }
    }

    private ResultadoDespacho terminar(String ordenId, EstadoDespacho resultado) {
        estado = resultado;
        activities.registrarEstado(ordenId, resultado);
        return new ResultadoDespacho(ordenId, resultado, List.copyOf(recibos), Set.copyOf(confirmadas));
    }

    private String comando(String ordenId, String unidad) {
        return ordenId + ":" + unidad;
    }

    @Override
    public void confirmarRecepcion(String unidad, String reciboId) {
        boolean reciboValido = recibos.stream()
                .anyMatch(r -> r.unidad().equals(unidad) && r.reciboId().equals(reciboId));
        if (reciboValido) confirmadas.add(unidad);
    }

    @Override
    public ProgresoDespacho consultarProgreso() {
        return new ProgresoDespacho(estado, recibos.size(), confirmadas.size(), total, Set.copyOf(confirmadas));
    }

    @Override
    public boolean cancelar(String motivo) {
        if (estado == EstadoDespacho.ENTREGADA || estado == EstadoDespacho.CANCELADA) return false;
        cancelada = motivo != null && !motivo.isBlank();
        return cancelada;
    }
}
