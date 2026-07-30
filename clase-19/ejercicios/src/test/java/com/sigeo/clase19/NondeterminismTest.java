package com.sigeo.clase19;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class NondeterminismTest {

    @Test
    void testGenerarId() {
        Nondeterminism nondeterminism = new Nondeterminism();
        
        // TODO(C19-E04): Verificar que el ID se genera de forma determinista
        String id = nondeterminism.generarId();
        
        assertThat(id).isNotNull();
        throw new UnsupportedOperationException("TODO C19-E04");
    }
}
