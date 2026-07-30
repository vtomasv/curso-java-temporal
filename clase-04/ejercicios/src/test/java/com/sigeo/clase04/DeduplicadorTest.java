package com.sigeo.clase04;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class DeduplicadorTest {

    @Test
    void debeEliminarDuplicadosYConservarOrden() {
        Deduplicador deduplicador = new Deduplicador();
        
        Solicitud s1 = new Solicitud("REQ-001", "Instalar software", "NUEVO", 1, 2);
        Solicitud s2 = new Solicitud("REQ-002", "Configurar red", "EN_PROGRESO", 2, 4);
        Solicitud s3 = new Solicitud("REQ-001", "Instalar software (duplicado)", "NUEVO", 1, 2);
        Solicitud s4 = new Solicitud("REQ-003", "Revisar logs", "CERRADO", 3, 1);
        
        List<Solicitud> entrada = List.of(s1, s2, s3, s4);
        
        List<Solicitud> resultado = deduplicador.deduplicar(entrada);
        
        assertThat(resultado)
            .hasSize(3)
            .containsExactly(s1, s2, s4);
    }
}
