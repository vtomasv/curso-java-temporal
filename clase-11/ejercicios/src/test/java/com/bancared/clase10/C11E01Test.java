package com.bancared.clase10;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e01") class C11E01Test {
    @Test void mismoDiaSinCompararAnio(){assertTrue(PoliticaCumple.corresponde(LocalDate.parse("1990-10-08"),LocalDate.parse("2026-10-08")));assertFalse(PoliticaCumple.corresponde(LocalDate.parse("1990-10-08"),LocalDate.parse("2026-10-09")));}
    @Test void bisiestoYReglaExplicita(){assertTrue(PoliticaCumple.corresponde(LocalDate.parse("2000-02-29"),LocalDate.parse("2027-02-28")));assertTrue(PoliticaCumple.corresponde(LocalDate.parse("2000-02-29"),LocalDate.parse("2028-02-29")));assertFalse(PoliticaCumple.corresponde(LocalDate.parse("2000-02-29"),LocalDate.parse("2028-02-28")));}
}
