package com.sigeo.clase06;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contacto contacto = (Contacto) o;
        return Objects.equals(email, contacto.email) && Objects.equals(telefono, contacto.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, telefono);
    }
}
