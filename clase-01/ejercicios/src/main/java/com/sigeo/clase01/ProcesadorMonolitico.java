package com.sigeo.clase01;

import java.util.List;

public class ProcesadorMonolitico {

    /**
     * Este método hace demasiadas cosas: valida, calcula y formatea.
     * 
     * TODO(C01-E05): Refactorizar extrayendo métodos más pequeños:
     * 1. boolean esValido(String dato)
     * 2. int calcularPuntaje(String dato)
     * 3. String formatearResultado(String dato, int puntaje)
     * 
     * El comportamiento final de procesarDatos debe ser exactamente el mismo.
     */
    public List<String> procesarDatos(List<String> datos) {
        
        List<String> resultados = new java.util.ArrayList<>();
        for (String dato : datos) {
            // Validación
            if (!esValido(dato)) {
                continue;
            }
            // Cálculo
            int puntaje = this.calcularPuntaje(dato);
            // Formateo
            String resultado = this.formatearResultado(dato, puntaje);
            resultados.add(resultado);
        }
        return resultados;

    }
    
    private boolean esValido(String dato) {
        return dato != null && !dato.trim().isEmpty() && dato.length() >= 3;
    }

    private int calcularPuntaje(String dato) {
        int puntaje = 0;
        for (char c : dato.toCharArray()) {
            if (Character.isUpperCase(c)) {
                puntaje += 2;
            } else {
                puntaje += 1;
            }
        }
        return puntaje;
    }
    
    private String formatearResultado(String dato, int puntaje) {
        return "DATO: " + dato.toUpperCase() + " | PUNTAJE: " + puntaje;
    }
}
