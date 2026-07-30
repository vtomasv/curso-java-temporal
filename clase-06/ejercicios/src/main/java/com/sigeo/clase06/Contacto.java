package com.sigeo.clase06;

import jakarta.persistence.Embeddable;
import java.util.Objects;

// TODO(C06-E02): Mapear como @Embeddable
public class Contacto {

    private String email;
    private String telefono;

    protected Contacto() {
        // JPA
    }

    public Contacto(String email, String telefono) {
        this.email = email;
        this.telefono = telefono;
    }

    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }

    // TODO(C06-E02): Implementar equals y hashCode basados en los valores (Value Object)
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("TODO C06-E02");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("TODO C06-E02");
    }
}
