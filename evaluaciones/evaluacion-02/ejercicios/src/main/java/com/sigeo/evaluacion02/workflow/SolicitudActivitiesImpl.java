package com.sigeo.evaluacion02.workflow;

import io.temporal.failure.ApplicationFailure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SolicitudActivitiesImpl implements SolicitudActivities {

    private final Map<String, String> resultadosPorClave = new ConcurrentHashMap<>();
    private final AtomicInteger efectosRegistrados = new AtomicInteger();

    @Override
    public String registrar(SolicitudWorkflowInput input) {
        if (input == null
                || input.id() == null || input.id().isBlank()
                || input.descripcion() == null || input.descripcion().isBlank()
                || input.claveIdempotencia() == null || input.claveIdempotencia().isBlank()) {
            throw ApplicationFailure.newNonRetryableFailure(
                    "El id, la descripción y la clave de idempotencia son obligatorios.", "VALIDATION");
        }

        return resultadosPorClave.computeIfAbsent(input.claveIdempotencia(), clave -> {
            efectosRegistrados.incrementAndGet();
            return "REG-" + input.id();
        });
    }

    @Override
    public void notificar(String solicitudId) {
        // Simulación sin I/O externo para la prueba.
    }

    public int efectosRegistrados() {
        return efectosRegistrados.get();
    }
}
