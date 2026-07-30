package com.sigeo.clase14;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NormativaRagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public NormativaRagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    /**
     * E04: Asistente de normativa (Ingesta)
     * Ingerir documentos en el vector store.
     */
    public void ingerirDocumentos(List<Document> documentos) {
        // TODO(C14-E04): Implementar la ingesta de documentos en el vectorStore
        throw new UnsupportedOperationException("TODO C14-E04");
    }

    /**
     * E04: Asistente de normativa (Consulta)
     * Recuperar fragmentos y responder con referencias internas.
     */
    public String consultarNormativa(String pregunta) {
        // TODO(C14-E04): Implementar retrieval de documentos similares y llamada al LLM con el contexto
        // El prompt debe instruir al modelo a responder "no encontrado" si la info no está en el contexto
        throw new UnsupportedOperationException("TODO C14-E04");
    }
}
