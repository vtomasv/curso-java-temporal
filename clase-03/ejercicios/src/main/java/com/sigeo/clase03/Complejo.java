package com.sigeo.clase03;

import java.io.PrintStream;
import java.util.ArrayList;

public class Complejo extends Menu {



    private ArrayList<Menu> items = null; 


    public Complejo(String nombre) {
        super(nombre);
        this.setItems(new ArrayList<Menu>());
    }

    private void setItems(ArrayList<Menu> items) {
        this.items = items;
    }

    private ArrayList<Menu> getItems() {
        return items;
    }

    public void addMenu(Menu item) {
        this.getItems().add(item);
    }

    @Override
    public void mostrar(PrintStream out, int profundidad, String sangria, int enumeracion) {
        String prefijo = enumeracion != 0 ? enumeracion + ") " : "";
        out.println(sangria + prefijo + this.getNombre());
        if (profundidad != 0) {
            for (Menu item : this.getItems()) {
                enumeracion = enumeracion + 1;
                item.mostrar(out, profundidad - 1, sangria + "   ", enumeracion);
            }
        }

    }



}
