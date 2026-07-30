package com.sigeo.clase19;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ErroresComunesTest {

    @Test
    void testProcesarDatos() {
        ErroresComunes errores = new ErroresComunes();
        
        // TODO(C19-E06): Verificar que la excepción se maneja correctamente
        errores.procesarDatos();
        
        throw new UnsupportedOperationException("TODO C19-E06");
    }
}
