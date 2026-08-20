package com.sigeo.clase04;

import java.net.URI;
import java.net.http.*;

public class ClienteCatalogo {

    private final String baseUrl;

    public ClienteCatalogo(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Consulta el catálogo por ID usando HttpClient.
     * Debe tener un timeout de 2 segundos.
     * Si la respuesta es 200, retorna el cuerpo como String.
     * Si la respuesta es 4xx o 5xx, lanza una RuntimeException con el status code.
     * 
     * @param id ID del elemento a consultar
     * @return Cuerpo de la respuesta
     */
    public String consultar(String id) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(2))
                .build();

        HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .timeout(java.time.Duration.ofSeconds(2))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Error en la respuesta: " + response.statusCode());   
            }
        } catch (java.io.IOException | java.lang.InterruptedException e) {
            throw new RuntimeException("Error en la solicitud HTTP", e);            
        }   
                
    }
}
