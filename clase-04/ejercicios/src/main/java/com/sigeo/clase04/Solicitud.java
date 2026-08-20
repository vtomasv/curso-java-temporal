package com.sigeo.clase04;

import java.util.Objects;

public record Solicitud(String id, String descripcion, String estado, int prioridad, int horasEstimadas) {
    
    public Solicitud {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Objects.requireNonNull(descripcion, "La descripción no puede ser nula");
        Objects.requireNonNull(estado, "El estado no puede ser nulo");
    }
    
    
    @Override
    public boolean equals(Object o) {
       
        if (o.getClass() != Solicitud.class) {
            return false;
        }
        Solicitud other = (Solicitud) o;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
