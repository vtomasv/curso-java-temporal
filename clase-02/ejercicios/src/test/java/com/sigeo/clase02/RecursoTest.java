package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class RecursoTest {

    @Test
    void constructor_ConDatosValidos_CreaRecurso() {
        Recurso recurso = new Recurso("R001", "Proyector", 5, "DISPONIBLE");
        
        assertThat(recurso.getCodigo()).isEqualTo("R001");
        assertThat(recurso.getNombre()).isEqualTo("Proyector");
        assertThat(recurso.getCantidad()).isEqualTo(5);
        assertThat(recurso.getEstado()).isEqualTo("DISPONIBLE");
    }

    @Test
    void constructor_ConCodigoVacio_LanzaExcepcion() {
        assertThatThrownBy(() -> new Recurso("", "Proyector", 5, "DISPONIBLE"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("codigo");
            
        assertThatThrownBy(() -> new Recurso(null, "Proyector", 5, "DISPONIBLE"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("codigo");
    }

    @Test
    void constructor_ConCantidadNegativa_LanzaExcepcion() {
        assertThatThrownBy(() -> new Recurso("R001", "Proyector", -1, "DISPONIBLE"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cantidad");
    }

    @Test
    void agregarCantidad_ConValorPositivo_AumentaCantidad() {
        Recurso recurso = new Recurso("R001", "Proyector", 5, "DISPONIBLE");
        recurso.agregarCantidad(3);
        assertThat(recurso.getCantidad()).isEqualTo(8);
    }

    @Test
    void consumirCantidad_ConValorValido_DisminuyeCantidad() {
        Recurso recurso = new Recurso("R001", "Proyector", 5, "DISPONIBLE");
        recurso.consumirCantidad(2);
        assertThat(recurso.getCantidad()).isEqualTo(3);
    }

    @Test
    void consumirCantidad_MayorQueDisponible_LanzaExcepcion() {
        Recurso recurso = new Recurso("R001", "Proyector", 5, "DISPONIBLE");
        assertThatThrownBy(() -> recurso.consumirCantidad(6))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
