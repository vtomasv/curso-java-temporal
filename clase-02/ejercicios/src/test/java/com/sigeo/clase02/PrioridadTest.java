package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;

class PrioridadTest {

    @Test
    void deadlineFrom_CalculaFechaCorrecta() {
        LocalDateTime inicio = LocalDateTime.of(2026, 7, 30, 10, 0);
        
        // Asumiendo que CRITICA tiene 4 horas de atención
        LocalDateTime deadlineCritica = Prioridad.CRITICA.deadlineFrom(inicio);
        assertThat(deadlineCritica).isEqualTo(LocalDateTime.of(2026, 7, 30, 14, 0));
        
        // Asumiendo que ALTA tiene 24 horas de atención
        LocalDateTime deadlineAlta = Prioridad.ALTA.deadlineFrom(inicio);
        assertThat(deadlineAlta).isEqualTo(LocalDateTime.of(2026, 7, 31, 10, 0));
    }

    @Test
    void getFactorEscalamiento_RetornaValorCorrecto() {
        assertThat(Prioridad.CRITICA.getFactorEscalamiento()).isGreaterThan(Prioridad.BAJA.getFactorEscalamiento());
    }
}
