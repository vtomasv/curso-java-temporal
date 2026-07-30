package com.sigeo.clase04;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IndiceResponsablesTest {

    @Test
    void debeRetornarListaVaciaParaResponsableSinAsignaciones() {
        IndiceResponsables indice = new IndiceResponsables(Map.of());
        Responsable r = new Responsable("111-1", "Juan", "TI");
        
        List<Solicitud> resultado = indice.obtenerSolicitudes(r);
        
        assertThat(resultado).isEmpty();
    }

    @Test
    void debeRetornarSolicitudesAsignadas() {
        Responsable r = new Responsable("111-1", "Juan", "TI");
        Solicitud s = new Solicitud("REQ-1", "Desc", "NUEVO", 1, 1);
        
        Map<Responsable, List<Solicitud>> mapa = new HashMap<>();
        mapa.put(r, List.of(s));
        
        IndiceResponsables indice = new IndiceResponsables(mapa);
        
        assertThat(indice.obtenerSolicitudes(r)).containsExactly(s);
    }

    @Test
    void debeSerInmodificable() {
        Responsable r = new Responsable("111-1", "Juan", "TI");
        Solicitud s = new Solicitud("REQ-1", "Desc", "NUEVO", 1, 1);
        
        Map<Responsable, List<Solicitud>> mapa = new HashMap<>();
        mapa.put(r, List.of(s));
        
        IndiceResponsables indice = new IndiceResponsables(mapa);
        
        // Modificar el mapa original no debería afectar al índice
        mapa.clear();
        
        assertThat(indice.obtenerSolicitudes(r)).containsExactly(s);
    }
}
