package com.sigeo.clase04;

import java.net.URI;

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
        // TODO(C04-E05): Implementar llamada HTTP con HttpClient
        // Pista: HttpClient.newBuilder().connectTimeout(...).build(), HttpRequest.newBuilder().timeout(...).build()
        throw new UnsupportedOperationException("TODO C04-E05");
    }
}
