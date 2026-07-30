package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ProcesadorEstadoTest {

    @Test
    void describirEstado_ManejaTodosLosCasos() {
        // TODO(C02-E06): Descomentar cuando EstadoSolicitud sea sealed
        /*
        ProcesadorEstado procesador = new ProcesadorEstado();
        
        EstadoSolicitud borrador = new Borrador("Falta firma");
        assertThat(procesador.describirEstado(borrador)).contains("Borrador", "Falta firma");
        
        EstadoSolicitud rechazada = new Rechazada("Sin fondos");
        assertThat(procesador.describirEstado(rechazada)).contains("Rechazada", "Sin fondos");
        */
        fail("TODO C02-E06: Implementar sealed interface y pattern matching");
    }
}
