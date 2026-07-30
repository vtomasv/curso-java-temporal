package com.sigeo.clase03;

import java.time.LocalDate;

public class PriorityCalculator {

    public int calculatePriority(int basePriority, LocalDate date) {
        // TODO(C03-E04): Depurar este método.
        // Hay un bug: si la prioridad base es máxima (10) y es fin de mes (día 31),
        // el cálculo lanza ArithmeticException por división por cero.
        // Corregir el código para que devuelva 100 en ese caso específico en lugar de fallar.
        
        int multiplier = 10;
        if (date.getDayOfMonth() == 31) {
            multiplier = 10 - basePriority; // Bug: si basePriority es 10, multiplier es 0
        }
        
        return 1000 / multiplier;
    }
}
