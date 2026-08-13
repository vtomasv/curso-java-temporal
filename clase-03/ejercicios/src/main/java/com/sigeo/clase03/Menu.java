package com.sigeo.clase03;

import java.io.PrintStream;

public abstract class Menu {

    // Atributos de la clase Menu
    // indicamos el nombre que tendra el menu
    private String nombre;

    private Accion accion = new AccionNula();

    public Accion getAccion() {
        return accion;
    }

    public void setAccion(Accion accion) {
        this.accion = accion;
    }

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

    public Menu ejecutarAccion() {
        this.getAccion().ejecutar();
        return this;
    }

    protected abstract Menu getMenu(int opcionInt);

    protected  int getNumeracion(int enumeracion)
    {
        return enumeracion + 1;
    }


}
