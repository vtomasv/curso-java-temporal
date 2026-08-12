package com.sigeo.clase01;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SolicitudTest {

    // TODO(C01-E04): Quitar @Disabled cuando el record esté implementado
    @Test
    void debeCrearSolicitudValida() {
        Solicitud s = new Solicitud("REQ-001", "Capitán Pérez", "Revisión de equipo", 2, LocalDate.of(2026, 7, 23));
        
        assertThat(s.id()).isEqualTo("REQ-001");
        assertThat(s.solicitante()).isEqualTo("Capitán Pérez");
        assertThat(s.descripcion()).isEqualTo("Revisión de equipo");
        assertThat(s.prioridad()).isEqualTo(2);
        assertThat(s.fecha()).isEqualTo(LocalDate.of(2026, 7, 23));
        
        assertThat(s.resumen()).isEqualTo("[REQ-001] Solicitud de Capitán Pérez (Prioridad: 2) - 2026-07-23");
    }

    @Test
    void debeValidarCamposObligatorios() {
        assertThatThrownBy(() -> new Solicitud(null, "Capitán Pérez", "Desc", 2, LocalDate.now()))
            .isInstanceOf(IllegalArgumentException.class);
            
        assertThatThrownBy(() -> new Solicitud("REQ-001", "", "Desc", 2, LocalDate.now()))
            .isInstanceOf(IllegalArgumentException.class);
            
        assertThatThrownBy(() -> new Solicitud("REQ-001", "Capitán Pérez", "Desc", 6, LocalDate.now()))
            .isInstanceOf(IllegalArgumentException.class);
            
        assertThatThrownBy(() -> new Solicitud("REQ-001", "Capitán Pérez", "Desc", 2, null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
