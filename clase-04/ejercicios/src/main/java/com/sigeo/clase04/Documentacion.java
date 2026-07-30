package com.sigeo.clase04;

/**
 * Clase de ejemplo para demostrar la documentación viva.
 * 
 * TODO(C04-E08): Completar el JavaDoc explicando cómo usar esta clase
 * y cómo reproducir un fallo común. Incluir un ejemplo de curl futuro.
 * 
 * Ejemplo de curl futuro:
 * {@code curl -X POST http://localhost:8080/api/solicitudes -d '{"id":"1"}'}
 */
public class Documentacion {

    /**
     * Procesa un elemento.
     * 
     * @param elemento El elemento a procesar
     * @return El resultado del procesamiento
     */
    public String procesar(String elemento) {
        return "Procesado: " + elemento;
    }
}
