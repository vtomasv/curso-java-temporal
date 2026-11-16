package com.enlace.evaluacion03.workflow;

import com.enlace.evaluacion03.config.Decisiones;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
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
    private EstadoDespacho estado = EstadoDespacho.ESPERANDO_FIRMAS;
    private FirmaProgreso firmas = new FirmaProgreso(0, 1, false, Set.of());
    private String ordenId;
    private int total;
    private boolean cancelada;
    private boolean despachoSolicitado;
    private boolean despachoIniciado;
    private boolean firmasCargadas;
    private boolean firmaEnCurso;

    @Override
    public ResultadoDespacho iniciar(DespachoInput input) {
        ordenId = input.ordenId();
        total = input.unidades().size();
        estado = EstadoDespacho.ESPERANDO_FIRMAS;
        firmas = activities.consultarFirmas(input.ordenId());
        firmasCargadas = true;

        Workflow.await(() -> firmas.completa() && !firmaEnCurso);
        estado = EstadoDespacho.PREPARANDO;
        if (!despachoSolicitado) {
            despachoSolicitado = true;
            activities.iniciarDespacho(input.ordenId());
            despachoIniciado = true;
        }
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

    @Override
    public void validarRegistrarFirma(String firmante, String rol) {
        firmante = texto(firmante, "firmante", 120);
        texto(rol, "rol", 80);

        // Un Update puede llegar mientras el método principal aún carga las firmas.
        // En ese caso el handler esperará firmasCargadas antes de decidir.
        if (!firmasCargadas) return;
        String motivo = motivoRechazoFirma(firmante);
        if (motivo != null) throw new IllegalStateException(motivo);
    }

    @Override
    public FirmaProgreso registrarFirma(String firmante, String rol) {
        Workflow.await(() -> firmasCargadas && !firmaEnCurso);
        firmaEnCurso = true;
        FirmaProgreso resultado;
        try {
            firmante = texto(firmante, "firmante", 120);
            rol = texto(rol, "rol", 80);
            String motivo = motivoRechazoFirma(firmante);
            if (motivo != null) {
                // Dos Updates pueden superar el validador mientras el primero espera
                // una Activity. Esta segunda comprobación cubre la carrera sin hacer
                // fallar y reintentar el Workflow Task.
                throw ApplicationFailure.newNonRetryableFailure(
                        motivo, "FIRMA_RECHAZADA");
            }

            // Un Update puede ceder mientras espera una Activity. Este cerrojo durable,
            // construido con Workflow.await, evita snapshots de firmas fuera de orden.
            firmas = activities.registrarFirma(ordenId, firmante, rol);
            resultado = firmas;
        } finally {
            firmaEnCurso = false;
        }

        // El último firmante recibe confirmación sólo después de que el método principal
        // haya realizado una única transición durable al despacho.
        if (resultado.completa()) Workflow.await(() -> despachoIniciado);
        return resultado;
    }

    private String motivoRechazoFirma(String firmante) {
        if (estado != EstadoDespacho.ESPERANDO_FIRMAS || firmas.completa()) {
            return "La orden ya cerró su etapa de firmas";
        }
        if (firmas.firmantes().contains(firmante)) {
            return "El usuario ya firmó la orden";
        }
        return null;
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
    public List<ReciboEnvio> consultarRecibos() {
        return List.copyOf(recibos);
    }

    @Override
    public FirmaProgreso consultarFirmas() {
        return firmas;
    }

    @Override
    public boolean cancelar(String motivo) {
        if (estado == EstadoDespacho.ESPERANDO_FIRMAS
                || estado == EstadoDespacho.ENTREGADA
                || estado == EstadoDespacho.CANCELADA) return false;
        cancelada = motivo != null && !motivo.isBlank();
        return cancelada;
    }

    private String texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo);
        }
        return valor.trim();
    }
}
