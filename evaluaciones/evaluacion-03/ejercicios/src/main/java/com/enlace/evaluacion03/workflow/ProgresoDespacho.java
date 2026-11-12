package com.enlace.evaluacion03.workflow;

import java.util.Set;

public record ProgresoDespacho(EstadoDespacho estado, int enviadas, int confirmadas,
                               int total, Set<String> unidadesConfirmadas) {
    public ProgresoDespacho {
        if (estado == null) throw new IllegalArgumentException("estado");
        if (enviadas < 0 || confirmadas < 0 || total < 0
                || enviadas > total || confirmadas > enviadas) {
            throw new IllegalArgumentException("contadores de progreso");
        }
        if (unidadesConfirmadas == null || unidadesConfirmadas.size() != confirmadas) {
            throw new IllegalArgumentException("unidadesConfirmadas");
        }
        unidadesConfirmadas = Set.copyOf(unidadesConfirmadas);
    }
}
