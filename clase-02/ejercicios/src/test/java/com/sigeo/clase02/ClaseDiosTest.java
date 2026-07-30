package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ClaseDiosTest {

    @Test
    void procesarTodo_MontoMenorA500000_ApruebaAutomaticamente() {
        ClaseDios sistema = new ClaseDios();
        sistema.procesarTodo("Juan", "juan@sigeo.mil.cl", "12345678-9", 1, 100000);
        
        assertThat(sistema.estadoSolicitud).isEqualTo("APROBADA_AUTOMATICA");
        assertThat(sistema.logs).contains("Solicitud 1 aprobada automática");
    }

    @Test
    void procesarTodo_MontoMayorA500000_RequiereGerencia() {
        ClaseDios sistema = new ClaseDios();
        sistema.procesarTodo("Ana", "ana@sigeo.mil.cl", "98765432-1", 2, 600000);
        
        assertThat(sistema.estadoSolicitud).isEqualTo("REQUIERE_APROBACION_GERENCIA");
        assertThat(sistema.logs).contains("Solicitud 2 requiere gerencia");
    }
    
    // TODO(C02-E08): Actualizar tests después de refactorizar
}
