package com.sigeo.clase14;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ClasificadorService.class})
class ClasificadorServiceTest {

    @Autowired
    private ClasificadorService clasificadorService;

    @MockBean
    private ChatClient.Builder chatClientBuilder;

    @MockBean
    private ChatClient chatClient;

    @Test
    void testClasificarSolicitud() {
        // TODO(C14-E01): Configurar mock para devolver JSON válido y verificar que se mapea a DTO
        // Este test fallará hasta que se implemente el TODO en ClasificadorService
        
        // Simulación básica para que compile
        ClasificadorService.ClasificacionDTO resultado = null;
        try {
            resultado = clasificadorService.clasificarSolicitud("Necesito ayuda con mi cuenta urgente");
        } catch (UnsupportedOperationException e) {
            // Ignorar para que el test falle en el assert
        }
        
        assertThat(resultado).isNotNull();
        assertThat(resultado.categoria()).isEqualTo(ClasificadorService.Categoria.SOPORTE);
        assertThat(resultado.urgencia()).isEqualTo(ClasificadorService.Urgencia.ALTA);
    }

    @Test
    void testClasificarConFallback() {
        // TODO(C14-E02): Configurar mock para lanzar excepción y verificar que se devuelve fallback
        
        ClasificadorService.ClasificacionDTO resultado = null;
        try {
            resultado = clasificadorService.clasificarConFallback("Mensaje de prueba");
        } catch (UnsupportedOperationException e) {
            // Ignorar
        }
        
        assertThat(resultado).isNotNull();
        assertThat(resultado.categoria()).isEqualTo(ClasificadorService.Categoria.OTRO);
        assertThat(resultado.urgencia()).isEqualTo(ClasificadorService.Urgencia.BAJA);
    }
}
