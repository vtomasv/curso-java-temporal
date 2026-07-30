package com.sigeo.clase02;

import java.util.List;
import java.util.ArrayList;

public class Vehiculo {
    private String patente;
    // TODO(C02-E05): Reemplazar herencia frágil (Ambulancia extends Vehiculo) 
    // y flags booleanos (esAmbulancia, tieneSirena) por composición usando Capacidad
    
    private boolean esAmbulancia;
    private boolean tieneSirena;
    
    public Vehiculo(String patente) {
        this.patente = patente;
    }
    
    // TODO(C02-E05): Implementar agregarCapacidad(Capacidad c) y tieneCapacidad(Class<? extends Capacidad> tipo)
    
    public void encenderSirena() {
        // TODO(C02-E05): Refactorizar para usar capacidades en lugar de flags
        if (esAmbulancia || tieneSirena) {
            System.out.println("Sirena encendida");
        } else {
            throw new IllegalStateException("El vehículo no tiene sirena");
        }
    }
}
