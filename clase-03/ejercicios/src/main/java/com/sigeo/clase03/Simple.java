package com.sigeo.clase03;

import java.io.PrintStream;

public class Simple extends Menu {

    // Atributos de la clase Simple
    private String descripcion;

    public Simple(String nombre, String descripcion) {
        super(nombre);
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void mostrar(PrintStream out, int profundidad, String sangria, int enumeracion) {
        out.println(sangria + enumeracion + ") " + this.getNombre());
    }

}
