package com.sigeo.evaluacion01;

/** Solicitud operativa inmutable del sistema SIGEO. */
public record Solicitud(
        String id,
        String solicitante,
        String descripcion,
        Prioridad prioridad
) {
    public Solicitud {
        // TODO(EV01-E02): Validar textos nulos o en blanco y prioridad nula.
        // Use mensajes que permitan identificar el campo incorrecto.
    }
}

