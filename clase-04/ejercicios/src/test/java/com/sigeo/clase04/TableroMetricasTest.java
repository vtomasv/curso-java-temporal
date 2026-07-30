package com.sigeo.clase04;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class TableroMetricasTest {

    @Test
    void debeCalcularMetricasSoloParaPrioridadAlta() {
        TableroMetricas tablero = new TableroMetricas();
        
        List<Solicitud> solicitudes = List.of(
            new Solicitud("1", "A", "NUEVO", 1, 10),
            new Solicitud("2", "B", "NUEVO", 2, 20),
            new Solicitud("3", "C", "NUEVO", 3, 100), // Ignorado (prioridad 3)
            new Solicitud("4", "D", "CERRADO", 1, 5)
        );
        
        Map<String, MetricasDTO> resultado = tablero.calcularMetricasPorEstado(solicitudes);
        
        assertThat(resultado).containsOnlyKeys("NUEVO", "CERRADO");
        
        assertThat(resultado.get("NUEVO").cantidad()).isEqualTo(2);
        assertThat(resultado.get("NUEVO").promedioHoras()).isCloseTo(15.0, within(0.01));
        
        assertThat(resultado.get("CERRADO").cantidad()).isEqualTo(1);
        assertThat(resultado.get("CERRADO").promedioHoras()).isCloseTo(5.0, within(0.01));
    }
}
