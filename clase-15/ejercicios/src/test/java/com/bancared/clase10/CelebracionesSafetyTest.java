package com.bancared.clase10;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class CelebracionesSafetyTest {
    @Test void reemplazoNoAcumulaMarcasNiDuplicados(){var s=new CelebracionesStore(NuevosTestSupport.jdbc());s.reemplazar("2026-10-08",List.of("A001"));s.reemplazar("2026-10-08",List.of("A001"));assertFalse(s.saludo("A001").isBlank());s.reemplazar("2026-10-09",List.of("B001"));assertEquals("",s.saludo("A001"));assertFalse(s.saludo("B001").isBlank());}
}
