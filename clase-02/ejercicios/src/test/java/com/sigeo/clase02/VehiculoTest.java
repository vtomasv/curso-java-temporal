package com.sigeo.clase02;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class VehiculoTest {

    @Test
    void vehiculoConSirena_PuedeEncenderSirena() {
        // TODO(C02-E05): Refactorizar test para usar composición
        /*
        Vehiculo v = new Vehiculo("AB-12-34");
        v.agregarCapacidad(new Sirena());
        
        assertThatCode(() -> v.encenderSirena()).doesNotThrowAnyException();
        */
        fail("TODO C02-E05: Implementar composición de capacidades");
    }

    @Test
    void vehiculoSinSirena_LanzaExcepcionAlEncender() {
        Vehiculo v = new Vehiculo("CD-56-78");
        
        assertThatThrownBy(() -> v.encenderSirena())
            .isInstanceOf(IllegalStateException.class);
    }
}
