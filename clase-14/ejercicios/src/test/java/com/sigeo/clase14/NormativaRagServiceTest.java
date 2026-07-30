package com.sigeo.clase14;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ai.vectorstore.VectorStore;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {NormativaRagService.class})
class NormativaRagServiceTest {

    @Autowired
    private NormativaRagService normativaRagService;

    @MockBean
    private VectorStore vectorStore;

    @Test
    void testConsultarNormativa() {
        // TODO(C14-E04): Configurar mock de VectorStore y ChatClient para probar el flujo RAG
        
        String respuesta = null;
        try {
            respuesta = normativaRagService.consultarNormativa("¿Cuál es el procedimiento de emergencia?");
        } catch (UnsupportedOperationException e) {
            // Ignorar
        }
        
        assertThat(respuesta).isNotNull();
        assertThat(respuesta).contains("no encontrado"); // Asumiendo que el mock no devuelve documentos relevantes
    }
}
