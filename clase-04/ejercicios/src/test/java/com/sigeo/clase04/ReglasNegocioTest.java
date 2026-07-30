package com.sigeo.clase04;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;

class ReglasNegocioTest {

    // TODO(C04-E07): Implementar ParameterizedTest con al menos 12 casos
    // Pista: Usar @CsvSource para definir los casos de prueba
    
    @ParameterizedTest(name = "Prioridad {0}, {1} horas, estado {2} -> cumple SLA: {3}")
    @CsvSource({
        // prioridad, horas, estado, resultadoEsperado
        "1, 20, NUEVO, true",
        "1, 25, NUEVO, false",
        "1, 25, CERRADO, true"
        // Agregar más casos hasta llegar a 12
    })
    void debeEvaluarSLA(int prioridad, int horas, String estado, boolean resultadoEsperado) {
        ReglasNegocio reglas = new ReglasNegocio();
        assertThat(reglas.cumpleSLA(prioridad, horas, estado)).isEqualTo(resultadoEsperado);
    }
}
