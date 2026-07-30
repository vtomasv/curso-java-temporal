package com.sigeo.clase01;

import java.time.LocalDate;

/**
 * Record que representa una Solicitud Operativa.
 * Debe contener: id (String), solicitante (String), descripcion (String), prioridad (int), fecha (LocalDate).
 */
// TODO(C01-E04): Convertir esta clase en un record con los campos solicitados
public class Solicitud {
    
    // TODO(C01-E04): Implementar constructor compacto que valide:
    // - id no nulo ni vacío
    // - solicitante no nulo ni vacío
    // - prioridad entre 1 y 5
    // - fecha no nula
    
    /**
     * Retorna un resumen legible de la solicitud.
     * Ejemplo: "[ID-123] Solicitud de Juan (Prioridad: 1) - 2026-07-23"
     */
    public String resumen() {
        // TODO(C01-E04): Implementar el método resumen
        throw new UnsupportedOperationException("TODO C01-E04");
    }
}
