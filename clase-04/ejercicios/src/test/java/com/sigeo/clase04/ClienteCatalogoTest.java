package com.sigeo.clase04;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClienteCatalogoTest {

    @Test
    @Disabled("Requiere servidor stub local para ejecutarse")
    void debeLanzarExcepcionEnErrorHttp() {
        // Asumiendo que http://localhost:9999/api/catalogo/error devuelve 404
        ClienteCatalogo cliente = new ClienteCatalogo("http://localhost:9999/api/catalogo");
        
        assertThatThrownBy(() -> cliente.consultar("error"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("404");
    }
}
