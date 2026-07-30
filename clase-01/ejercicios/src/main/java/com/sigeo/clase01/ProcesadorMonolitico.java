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
        // TODO(C01-E05): Reemplazar el contenido de este método usando los métodos extraídos
        throw new UnsupportedOperationException("TODO C01-E05");
        
        /* Código original a refactorizar:
        List<String> resultados = new java.util.ArrayList<>();
        for (String dato : datos) {
            // Validación
            if (dato == null || dato.trim().isEmpty() || dato.length() < 3) {
                continue;
            }
            
            // Cálculo
            int puntaje = 0;
            for (char c : dato.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    puntaje += 2;
                } else {
                    puntaje += 1;
                }
            }
            
            // Formateo
            String resultado = "DATO: " + dato.toUpperCase() + " | PUNTAJE: " + puntaje;
            resultados.add(resultado);
        }
        return resultados;
        */
    }
    
    // TODO(C01-E05): Crear método esValido
    
    // TODO(C01-E05): Crear método calcularPuntaje
    
    // TODO(C01-E05): Crear método formatearResultado
}
