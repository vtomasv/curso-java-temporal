package com.sigeo.clase03;

import java.io.PrintStream;

public class Simple extends Menu {

    // Atributos de la clase Simple
    private String descripcion;

    private Menu padre; 

    public Simple(String nombre, String descripcion, Menu padre) {
        super(nombre);
        this.descripcion = descripcion;
        this.padre = padre;
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

    @Override
    protected Menu getMenu(int opcionInt) {
        return this; // Un menú simple no tiene submenús, por lo que devuelve a sí mismo 
    }

    @Override
    public Menu ejecutarAccion() {
        this.getAccion().ejecutar();
        return this.padre;
    
    }

}
