package com.sigeo.clase03;

import java.io.PrintStream;

public class Linea extends Menu {

    public Linea(String nombre) {
        super(nombre);
    }   

    @Override
    public void mostrar(PrintStream out, int profundidad, String sangria, int enumeracion) {
        out.println(sangria + "----------");
    }

    @Override
    protected Menu getMenu(int opcionInt) {
        return this;
    }

    protected  int getNumeracion(int enumeracion)
    {
        return enumeracion;
    }


}
