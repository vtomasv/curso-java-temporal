package com.sigeo.clase14;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ClasificadorService {

    private final ChatClient chatClient;

    public ClasificadorService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public enum Categoria {
        SOPORTE, VENTAS, RECLAMO, OTRO
    }

    public enum Urgencia {
        ALTA, MEDIA, BAJA
    }

    public record ClasificacionDTO(Categoria categoria, Urgencia urgencia, String explicacion) {}

    /**
     * E01: Clasificador estructurado
     * Diseñar prompt que clasifique solicitud y devuelva DTO con categoría, urgencia y explicación breve.
     */
    public ClasificacionDTO clasificarSolicitud(String solicitud) {
        // TODO(C14-E01): Implementar llamada a Spring AI con structured output para devolver ClasificacionDTO
        throw new UnsupportedOperationException("TODO C14-E01");
    }

    /**
     * E02: Fallback sin IA
     * Ante timeout o salida inválida, usar clasificación determinista simple.
     */
    public ClasificacionDTO clasificarConFallback(String solicitud) {
        // TODO(C14-E02): Implementar llamada a clasificarSolicitud con try-catch y devolver fallback determinista en caso de error
        throw new UnsupportedOperationException("TODO C14-E02");
    }
}
