package com.sigeo.clase03;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ResilientServiceTest {

    @Test
    void shouldRetryAndSucceed() {
        UnstableRepository repo = new UnstableRepository();
        ResilientService service = new ResilientService(repo);
        
        // El repositorio fallará en la llamada 3, 6, 9...
        assertThat(service.getReliableData()).isEqualTo("Data OK"); // 1
        assertThat(service.getReliableData()).isEqualTo("Data OK"); // 2
        
        // La llamada 3 fallará internamente, pero el servicio debe reintentar (llamada 4) y tener éxito
        assertThat(service.getReliableData()).isEqualTo("Data OK");
    }
}
