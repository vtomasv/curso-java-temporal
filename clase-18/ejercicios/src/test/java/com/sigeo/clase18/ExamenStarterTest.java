package com.sigeo.clase18;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExamenStarterTest {

    @Test
    void testCambioRegla() {
        ExamenStarter starter = new ExamenStarter();
        // Este test fallará hasta que se implemente el TODO C18-E02
        assertThatThrownBy(() -> starter.implementarCambioRegla())
            .isNotInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testNuevaInteraccion() {
        ExamenStarter starter = new ExamenStarter();
        // Este test fallará hasta que se implemente el TODO C18-E03
        assertThatThrownBy(() -> starter.implementarNuevaInteraccion())
            .isNotInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testFalloInducido() {
        ExamenStarter starter = new ExamenStarter();
        // Este test fallará hasta que se implemente el TODO C18-E04
        assertThatThrownBy(() -> starter.corregirFalloInducido())
            .isNotInstanceOf(UnsupportedOperationException.class);
    }
}
