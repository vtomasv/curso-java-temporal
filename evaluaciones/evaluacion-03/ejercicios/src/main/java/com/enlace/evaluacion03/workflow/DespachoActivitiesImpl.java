package com.enlace.evaluacion03.workflow;

import com.enlace.evaluacion03.config.Decisiones;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DespachoActivitiesImpl implements DespachoActivities {

    private final Map<String, ReciboEnvio> recibos = new ConcurrentHashMap<>();
    private final Map<String, String> clavesPorRecibo = new ConcurrentHashMap<>();
    private final AtomicInteger efectos = new AtomicInteger();

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
        // Simulación didáctica. En una implementación real esta Activity persistiría el estado.
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
}
