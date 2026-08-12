package com.sigeo.clase01;

public class ClasificadorPrioridad {

    /**
     * Convierte un código numérico (1-5) en una descripción de prioridad.
     * 1: "Crítica - 1 hora"
     * 2: "Alta - 4 horas"
     * 3: "Media - 24 horas"
     * 4: "Baja - 72 horas"
     * 5: "Planificada - 1 semana"
     * Cualquier otro valor debe lanzar IllegalArgumentException.
     * 
     * REQUISITO: Usar switch expression (no usar cadena de if-else).
     */
    public static String priorityFor(int codigo) {
        switch (codigo) {
            case 1 -> {
                return "Crítica - 1 hora";
            }
            case 2 -> {
                return "Alta - 4 horas";
            }
            case 3 -> {
                return "Media - 24 horas";
            }
            case 4 -> {
                return "Baja - 72 horas";
            }
            case 5 -> {
                return "Planificada - 1 semana";
            }
            default -> throw new IllegalArgumentException("Código de prioridad inválido: " + codigo);
        }
    }
}
