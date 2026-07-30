package com.sigeo.clase04;

import java.util.List;

public class ProcesadorParalelo {

    /**
     * Procesa una lista de solicitudes en paralelo usando Virtual Threads.
     * Simula un procesamiento que toma 50ms por solicitud.
     * Debe retornar la suma total de las horas estimadas de todas las solicitudes procesadas.
     * 
     * @param solicitudes Lista de solicitudes a procesar
     * @return Suma total de horas estimadas
     */
    public int procesarYSumarHoras(List<Solicitud> solicitudes) {
        // TODO(C04-E06): Implementar procesamiento paralelo con Virtual Threads
        // Pista: Executors.newVirtualThreadPerTaskExecutor(), Future, o StructuredTaskScope (si se usa preview)
        // Asegurar que no haya race conditions al sumar.
        throw new UnsupportedOperationException("TODO C04-E06");
    }
}
