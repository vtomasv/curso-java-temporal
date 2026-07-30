package com.sigeo.clase14;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class AnalisisAiActivityImpl implements AnalisisAiActivity {

    private final ChatClient chatClient;

    public AnalisisAiActivityImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public AnalisisResponse analizarTexto(AnalisisRequest request) {
        // TODO(C14-E06): Implementar llamada al LLM y devolver AnalisisResponse
        // Manejar errores de forma que Temporal pueda reintentar (o no, si es permanente)
        throw new UnsupportedOperationException("TODO C14-E06");
    }
}
