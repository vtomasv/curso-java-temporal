package com.bancared.clase10;
import java.time.LocalDate;
import java.time.MonthDay;
public final class PoliticaCumple {
    private PoliticaCumple() {}
    public static boolean corresponde(LocalDate nacimiento, LocalDate fecha) {
        if(nacimiento==null||fecha==null)throw new IllegalArgumentException("Fechas obligatorias.");
        MonthDay cumple=MonthDay.from(nacimiento);
        if(cumple.equals(MonthDay.of(2,29))&&!fecha.isLeapYear())cumple=MonthDay.of(2,28);
        return cumple.equals(MonthDay.from(fecha));
    }
}
