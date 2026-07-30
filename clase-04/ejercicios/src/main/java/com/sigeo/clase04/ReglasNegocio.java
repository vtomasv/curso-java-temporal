package com.sigeo.clase04;

public class ReglasNegocio {

    /**
     * Determina si una solicitud cumple el SLA (Service Level Agreement).
     * Reglas:
     * - Prioridad 1: SLA es 24 horas.
     * - Prioridad 2: SLA es 48 horas.
     * - Prioridad 3: SLA es 72 horas.
     * - Si el estado es "CERRADO", siempre cumple el SLA (retorna true).
     * 
     * @param prioridad Prioridad de la solicitud (1, 2 o 3)
     * @param horasTranscurridas Horas desde la creación
     * @param estado Estado actual
     * @return true si cumple el SLA, false en caso contrario
     */
    public boolean cumpleSLA(int prioridad, int horasTranscurridas, String estado) {
        // TODO(C04-E07): Implementar la lógica de negocio
        throw new UnsupportedOperationException("TODO C04-E07");
    }
}
