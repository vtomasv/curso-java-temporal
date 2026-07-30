package com.sigeo.clase04;

import java.util.Objects;

public record Solicitud(String id, String descripcion, String estado, int prioridad, int horasEstimadas) {
    
    public Solicitud {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Objects.requireNonNull(descripcion, "La descripción no puede ser nula");
        Objects.requireNonNull(estado, "El estado no puede ser nulo");
    }
    
    // TODO(C04-E01): Implementar equals y hashCode basados únicamente en el 'id'
    // para asegurar la deduplicación correcta.
    
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("TODO C04-E01");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("TODO C04-E01");
    }
}
