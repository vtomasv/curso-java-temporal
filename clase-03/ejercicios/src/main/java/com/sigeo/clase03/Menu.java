package com.sigeo.clase03;

import java.io.PrintStream;

public abstract class Menu {

    // Atributos de la clase Menu
    // indicamos el nombre que tendra el menu
    private String nombre;

    public Menu(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract void mostrar(PrintStream out, int profundidad, String sangria, int enumeracion); 


}
