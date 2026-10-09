package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("base-c11-e03") class C11E03Test {
    @Test void calendarioDiarioDeChile(){var s=AgendaCumple.especificacion();assertEquals("America/Santiago",s.getTimeZoneName());assertEquals(1,s.getCalendars().size());var c=s.getCalendars().getFirst();assertEquals(0,c.getHour().getFirst().getStart());assertEquals(5,c.getMinutes().getFirst().getStart());}
}
