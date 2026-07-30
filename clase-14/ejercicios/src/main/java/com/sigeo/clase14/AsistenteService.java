package com.sigeo.clase14;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AsistenteService {

    private final ChatClient chatClient;

    public AsistenteService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * E03: Consulta de catálogo (Llamada)
     * Usar la herramienta consultarRecursos en una llamada al LLM.
     */
    public String consultarAsistente(String pregunta) {
        // TODO(C14-E03): Implementar llamada a Spring AI habilitando la tool "consultarRecursos"
        throw new UnsupportedOperationException("TODO C14-E03");
    }
}
