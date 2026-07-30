package com.sigeo.clase19;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cambios")
public class CambioCompatible {

    @Id
    private Long id;
    
    private String descripcion;
    
    // TODO(C19-E02): Agregar nuevo atributo con validación y seguridad
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
