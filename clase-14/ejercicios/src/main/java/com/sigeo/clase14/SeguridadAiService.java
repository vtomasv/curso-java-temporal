package com.sigeo.clase14;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SeguridadAiService {

    private final ChatClient chatClient;

    public SeguridadAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * E05: Prompt injection lab
     * Mitigar intentos de cambiar instrucciones mediante separación de roles.
     */
    public String procesarTextoSeguro(String textoUsuario) {
        // TODO(C14-E05): Implementar llamada al LLM usando System Prompt fuerte y User Prompt aislado
        // Asegurar que el modelo no ejecute comandos inyectados en textoUsuario
        throw new UnsupportedOperationException("TODO C14-E05");
    }
}
