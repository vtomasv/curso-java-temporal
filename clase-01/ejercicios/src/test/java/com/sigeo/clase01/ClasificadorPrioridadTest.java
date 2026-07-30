package com.sigeo.clase01;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClasificadorPrioridadTest {

    @ParameterizedTest
    @CsvSource({
        "1, 'Crítica - 1 hora'",
        "2, 'Alta - 4 horas'",
        "3, 'Media - 24 horas'",
        "4, 'Baja - 72 horas'",
        "5, 'Planificada - 1 semana'"
    })
    void debeRetornarPrioridadCorrecta(int codigo, String esperado) {
        assertThat(ClasificadorPrioridad.priorityFor(codigo)).isEqualTo(esperado);
    }

    @Test
    void debeLanzarExcepcionParaCodigoInvalido() {
        assertThatThrownBy(() -> ClasificadorPrioridad.priorityFor(0))
            .isInstanceOf(IllegalArgumentException.class);
            
        assertThatThrownBy(() -> ClasificadorPrioridad.priorityFor(6))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
