package com.enlace.evaluacion03.workflow;

import com.enlace.evaluacion03.config.Decisiones;
import io.temporal.failure.ApplicationFailure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DespachoActivitiesImpl implements DespachoActivities {

    private final Map<String, ReciboEnvio> recibos = new ConcurrentHashMap<>();
    private final Map<String, String> clavesPorRecibo = new ConcurrentHashMap<>();
    private final AtomicInteger efectos = new AtomicInteger();
    private final OrdenLifecycle ordenes;

    public DespachoActivitiesImpl() {
        this(new OrdenLifecycle() {
            @Override
            public FirmaProgreso consultarFirmas(String ordenId) {
                return FirmaProgreso.autorizadaParaPruebas();
            }

            @Override
            public FirmaProgreso registrarFirma(String ordenId, String firmante, String rol) {
                return FirmaProgreso.autorizadaParaPruebas();
            }

            @Override
            public void iniciarDespacho(String ordenId) {
                // Adaptador neutro para las pruebas unitarias de idempotencia.
            }

            @Override
            public void registrarEstado(String ordenId, EstadoDespacho estado) {
                // Adaptador neutro para las pruebas unitarias de idempotencia.
            }
        });
    }

    public DespachoActivitiesImpl(OrdenLifecycle ordenes) {
        if (ordenes == null) throw new IllegalArgumentException("ordenes");
        this.ordenes = ordenes;
    }

    @Override
    public FirmaProgreso consultarFirmas(String ordenId) {
        return ordenes.consultarFirmas(texto(ordenId, "ordenId"));
    }

    @Override
    public FirmaProgreso registrarFirma(String ordenId, String firmante, String rol) {
        try {
            return ordenes.registrarFirma(
                    texto(ordenId, "ordenId"),
                    texto(firmante, "firmante"),
                    texto(rol, "rol")
            );
        } catch (IllegalArgumentException | IllegalStateException exception) {
            // Es un rechazo de negocio, no un fallo transitorio. El tipo coincide
            // con doNotRetry("VALIDATION") de las opciones de la Activity.
            throw ApplicationFailure.newNonRetryableFailure(
                    exception.getMessage(), "VALIDATION");
        }
    }

    @Override
    public void iniciarDespacho(String ordenId) {
        ordenes.iniciarDespacho(texto(ordenId, "ordenId"));
    }

    @Override
    public ReciboEnvio enviar(String ordenId, String unidad, String comandoId) {
        ordenId = texto(ordenId, "ordenId");
        unidad = texto(unidad, "unidad");
        return switch (Decisiones.IDEMPOTENCIA) {
            case PENDIENTE -> crearUnaVez(ordenId + ":" + unidad, ordenId, unidad);
            case CLAVE_ORDEN_UNIDAD -> enviarConClaveNatural(ordenId, unidad);
            case COMANDO_EXPLICITO -> enviarConComando(ordenId, unidad, comandoId);
        };
    }

    private ReciboEnvio enviarConClaveNatural(String ordenId, String unidad) {
        // TODO(EV03-E05-A): implemente el camino cuya intención completa es orden+unidad.
        throw new UnsupportedOperationException("TODO EV03-E05-A");
    }

    private ReciboEnvio enviarConComando(String ordenId, String unidad, String comandoId) {
        // TODO(EV03-E05-B): implemente el camino con comando estable explícito.
        throw new UnsupportedOperationException("TODO EV03-E05-B");
    }

    private ReciboEnvio crearUnaVez(String clave, String ordenId, String unidad) {
        ReciboEnvio recibo = recibos.computeIfAbsent(clave, ignorada -> {
            int numero = efectos.incrementAndGet();
            return new ReciboEnvio(ordenId, unidad, "REC-" + numero);
        });
        clavesPorRecibo.putIfAbsent(recibo.reciboId(), clave);
        return recibo;
    }

    @Override
    public void registrarEstado(String ordenId, EstadoDespacho estado) {
        ordenes.registrarEstado(texto(ordenId, "ordenId"), estado);
    }

    @Override
    public void compensar(String ordenId, String unidad, String reciboId) {
        // Simulación didáctica e idempotente de una anulación conocida.
        String clave = clavesPorRecibo.get(reciboId);
        if (clave == null) return;
        ReciboEnvio esperado = new ReciboEnvio(ordenId, unidad, reciboId);
        if (recibos.remove(clave, esperado)) clavesPorRecibo.remove(reciboId, clave);
    }

    public int efectosRegistrados() {
        return efectos.get();
    }

    public int recibosActivos() {
        return recibos.size();
    }

    private static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException(campo);
        return valor.trim();
    }

    public interface OrdenLifecycle {
        FirmaProgreso consultarFirmas(String ordenId);
        FirmaProgreso registrarFirma(String ordenId, String firmante, String rol);
        void iniciarDespacho(String ordenId);
        void registrarEstado(String ordenId, EstadoDespacho estado);
    }
}
