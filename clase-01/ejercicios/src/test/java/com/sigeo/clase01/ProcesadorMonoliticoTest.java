package com.sigeo.clase01;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProcesadorMonoliticoTest {

    @Test
    void debeProcesarDatosCorrectamente() {
        ProcesadorMonolitico procesador = new ProcesadorMonolitico();
        
        List<String> entrada = Arrays.asList(
            "abc",      // válido, puntaje 3
            "AbC",      // válido, puntaje 5
            "ab",       // inválido (muy corto)
            "",         // inválido (vacío)
            null,       // inválido (null)
            "SIGEO"     // válido, puntaje 10
        );
        
        List<String> resultado = procesador.procesarDatos(entrada);
        
        assertThat(resultado).containsExactly(
            "DATO: ABC | PUNTAJE: 3",
            "DATO: ABC | PUNTAJE: 5",
            "DATO: SIGEO | PUNTAJE: 10"
        );
    }
}
