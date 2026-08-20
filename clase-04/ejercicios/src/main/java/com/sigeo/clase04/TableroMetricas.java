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

        return solicitudes.stream()
                .filter(s -> s.prioridad() == 1 || s.prioridad() == 2)
                .collect(java.util.stream.Collectors.groupingBy(
                        Solicitud::estado,
                        java.util.stream.Collectors.collectingAndThen(
                                java.util.stream.Collectors.toList(),
                                list -> {
                                    int cantidad = list.size();
                                    double promedioHoras = list.stream()
                                            .mapToInt(Solicitud::horasEstimadas)
                                            .average()
                                            .orElse(0.0);
                                    return new MetricasDTO(cantidad, promedioHoras);
                                }
                        )
                ));

    }
}
