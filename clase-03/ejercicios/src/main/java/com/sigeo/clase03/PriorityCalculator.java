package com.sigeo.clase03;

import java.time.LocalDate;

public class PriorityCalculator {

    public int calculatePriority(int basePriority, LocalDate date) {

        int multiplier = 10;
        try {
            if (date.getDayOfMonth() == 31) {
            multiplier = 10 - basePriority; // Bug: si basePriority es 10, multiplier es 0
            return 1000 / multiplier;
        }
        } catch (ArithmeticException e) {
            // Manejar la excepción de manera adecuada, por ejemplo, registrando un mensaje de error
            System.err.println("Error al calcular la prioridad: " + e.getMessage());
            // Devolver un valor predeterminado o lanzar una excepción personalizada si es necesario
        }

        return 100;
       
    }
}
