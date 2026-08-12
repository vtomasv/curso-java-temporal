package com.sigeo.clase01;

import java.time.LocalDate;

/**
 * Record que representa una Solicitud Operativa.
 * Debe contener: id (String), solicitante (String), descripcion (String), prioridad (int), fecha (LocalDate).
 */

public record Solicitud(
        String id,
        String solicitante,
        String descripcion,
        int prioridad,
        LocalDate fecha
) {
    public Solicitud {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede ser nulo o vacío");
        }
        if (solicitante == null || solicitante.isBlank()) {
            throw new IllegalArgumentException("El solicitante no puede ser nulo o vacío");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía");
        }
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
    }

    public String resumen() {
        return "[%s] Solicitud de %s (Prioridad: %d) - %s"
                .formatted(id, solicitante, prioridad, fecha);
    }
}