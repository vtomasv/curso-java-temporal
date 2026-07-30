package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class AgregadoSolicitudTest {

    @Test
    void aprobar_ConMontoValido_CambiaEstado() {
        AgregadoSolicitud solicitud = new AgregadoSolicitud(1000);
        
        // TODO(C02-E07): Descomentar cuando aprobar() esté implementado
        /*
        solicitud.aprobar();
        // Verificar estado (requiere getter o método de consulta)
        */
        fail("TODO C02-E07: Implementar validación con clase interna");
    }

    @Test
    void aprobar_ConMontoCero_LanzaExcepcion() {
        AgregadoSolicitud solicitud = new AgregadoSolicitud(0);
        
        assertThatThrownBy(() -> solicitud.aprobar())
            .isInstanceOf(IllegalStateException.class);
    }
}
