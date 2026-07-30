package com.sigeo.clase04;

import java.util.List;
import java.util.Map;

public class TableroMetricas {

    /**
     * Agrupa las solicitudes por estado y calcula métricas (cantidad y promedio de horas) para cada estado.
     * Solo debe considerar solicitudes con prioridad 1 o 2.
     * 
     * @param solicitudes Lista de solicitudes a procesar
     * @return Mapa con el estado como clave y las métricas como valor
     */
    public Map<String, MetricasDTO> calcularMetricasPorEstado(List<Solicitud> solicitudes) {
        // TODO(C04-E03): Implementar usando Streams sin efectos secundarios
        // Pista: filter, collect, groupingBy, teeing (o collect y luego transformar)
        throw new UnsupportedOperationException("TODO C04-E03");
    }
}
