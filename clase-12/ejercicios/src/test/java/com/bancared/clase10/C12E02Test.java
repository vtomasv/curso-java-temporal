package com.bancared.clase10;
import org.junit.jupiter.api.*;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e02") class C12E02Test {
    @Test void aprobarIniciaUnHijo(){try(var f=new AprobacionFixture()){var w=f.iniciar();w.decidir("d1","APROBAR");w.decidir("d1","APROBAR");assertEquals("COMPLETADA",f.resultado(w).estado());assertEquals(1,f.preparaciones.get());assertEquals(100000,f.montoHijo);}}
    @Test void vencimientoSinMovimientos(){try(var f=new AprobacionFixture()){var w=f.iniciar();f.env.sleep(Duration.ofSeconds(31));assertEquals("VENCIDA",f.resultado(w).estado());assertEquals(0,f.preparaciones.get());}}
    @Test void cancelarSinMovimientos(){try(var f=new AprobacionFixture()){var w=f.iniciar();w.decidir("d1","CANCELAR");assertEquals("CANCELADA",f.resultado(w).estado());assertEquals(0,f.preparaciones.get());}}
}
