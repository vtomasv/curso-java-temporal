package com.sigeo.clase14;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ClasificadorService.class})
class PresupuestoTest {

    @Autowired
    private Environment env;

    @Test
    void testConfiguracionModelo() {
        // TODO(C14-E08): Verificar que la configuración del modelo es la adecuada para el presupuesto
        String modelo = env.getProperty("spring.ai.openai.chat.options.model");
        
        assertThat(modelo).isNotNull();
        // Preferimos un modelo más barato/rápido para tareas simples
        assertThat(modelo).isEqualTo("gpt-4o-mini");
    }
}
