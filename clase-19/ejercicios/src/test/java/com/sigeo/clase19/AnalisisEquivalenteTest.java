package com.sigeo.clase19;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AnalisisEquivalenteTest {

    @Test
    void testAnalizar() {
        AnalisisEquivalente analisis = new AnalisisEquivalente();
        String[][] matriz = new String[12][12];
        
        String resultado = analisis.analizar(matriz);
        
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo("Análisis completado");
    }
}
