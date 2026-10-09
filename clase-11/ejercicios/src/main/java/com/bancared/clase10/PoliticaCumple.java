package com.bancared.clase10;
import java.time.LocalDate;
import java.time.MonthDay;
public final class PoliticaCumple {
    private PoliticaCumple() {}
    public static boolean corresponde(LocalDate nacimiento, LocalDate fecha) {
        // TODO(C11-E01): comparar mes/día. El 29/02 se celebra el 28/02 en años no bisiestos.
        return false;
    }
}
