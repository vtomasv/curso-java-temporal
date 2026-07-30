package com.sigeo.clase14;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CatalogoTools.class, AsistenteService.class})
class AsistenteServiceTest {

    @Autowired
    private CatalogoTools catalogoTools;

    @Test
    void testConsultarRecursosTool() {
        // TODO(C14-E03): Verificar que la tool funciona correctamente
        
        List<CatalogoTools.Recurso> recursos = null;
        try {
            recursos = catalogoTools.consultarRecursos().apply(new CatalogoTools.ConsultaRecursoRequest("vehiculo"));
        } catch (UnsupportedOperationException e) {
            // Ignorar
        }
        
        assertThat(recursos).isNotNull();
        assertThat(recursos).isNotEmpty();
    }
}
