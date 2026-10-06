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
        // TODO(EV02-E08):
        // - validar input, id, descripción y clave;
        // - para datos inválidos lanzar ApplicationFailure de tipo VALIDATION;
        // - retornar el mismo resultado para una clave repetida sin repetir el efecto;
        // - para una clave nueva guardar "REG-" + input.id().
        throw new UnsupportedOperationException("TODO EV02-E08");
    }

    @Override
    public void notificar(String solicitudId) {
        // Simulación sin I/O externo para la prueba.
    }

    public int efectosRegistrados() {
        return efectosRegistrados.get();
    }
}
