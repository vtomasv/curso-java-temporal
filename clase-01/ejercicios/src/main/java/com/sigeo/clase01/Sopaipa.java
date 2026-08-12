package com.sigeo.clase01;

import java.util.random.RandomGenerator;

public class Sopaipa {
    
    private static Integer contador = 0;
    private String id_sopaipa;

    public String getId_sopaipa() {
        return id_sopaipa;
    }

    public static Integer getContador() {
        return contador;
    }

    public static void incrementarContador() {
        contador++;
    }

    public Sopaipa() {
        incrementarContador();
        this.id_sopaipa = "Sopaipa-PRD-" + RandomGenerator.getDefault().nextInt(1, 1000);
    }
 
    public static void main(String[] args) {
        System.out.println("Contador inicial: " + Sopaipa.getContador());
        Sopaipa sopaipa1 = new Sopaipa();
        System.out.println("ID sopaipa 1: " + sopaipa1.getId_sopaipa());

        Sopaipa sopaipa2 = new Sopaipa();
        System.out.println("ID sopaipa 2: " + sopaipa2.getId_sopaipa());
        System.out.println("Contador final: " + Sopaipa.getContador());
    }
}
