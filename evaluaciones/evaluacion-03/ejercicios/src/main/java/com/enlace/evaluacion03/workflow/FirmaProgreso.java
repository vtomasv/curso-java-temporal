package com.enlace.evaluacion03.workflow;

import java.util.Set;

/** Estado durable de autorización de una orden, observable mediante Temporal. */
public record FirmaProgreso(int registradas, int requeridas, boolean completa,
                            Set<String> firmantes) {

    public FirmaProgreso {
        if (firmantes == null || firmantes.size() != registradas) {
            throw new IllegalArgumentException("firmantes");
        }
        if (registradas < 0 || requeridas < 1) {
            throw new IllegalArgumentException("contadores de firmas");
        }
        if (completa && registradas < requeridas) {
            throw new IllegalArgumentException("política de firmas incompleta");
        }
        firmantes = Set.copyOf(firmantes);
    }

    public static FirmaProgreso autorizadaParaPruebas() {
        return new FirmaProgreso(1, 1, true, Set.of("AUTORIZADOR_PRUEBA"));
    }
}
