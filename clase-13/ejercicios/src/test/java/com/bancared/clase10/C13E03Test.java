package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e03") class C13E03Test {
    @Test void liquidacionMueveSoloLiquidez(){try(var f=new LiquidacionFixture()){assertEquals("LIQUIDADO",f.ejecutar().estado());assertEquals(1900000,f.a.vista().saldo());assertEquals(2100000,f.b.vista().saldo());assertTrue(f.ts.obtener("tx-a").liquidada());}}
    @Test void rechazoConocidoCompensa(){try(var f=new LiquidacionFixture()){f.fallo="RECHAZO";assertEquals("COMPENSADO",f.ejecutar().estado());assertEquals(2000000,f.a.vista().saldo());assertEquals(2000000,f.b.vista().saldo());assertEquals(2,f.a.vista().movimientos().size());assertNull(f.ts.obtener("tx-a").lote());}}
    @Test void incertidumbreNoDevuelveDinero(){try(var f=new LiquidacionFixture()){f.fallo="INCIERTO";assertEquals("PENDIENTE_REVISION",f.ejecutar().estado());assertEquals(1900000,f.a.vista().saldo());assertEquals(2000000,f.b.vista().saldo());assertEquals("lote",f.ts.obtener("tx-a").lote());}}
    @Test void respuestaPerdidaRecuperaRecibo(){try(var f=new LiquidacionFixture()){f.fallo="RESPUESTA_PERDIDA";assertEquals("LIQUIDADO",f.ejecutar().estado());assertEquals(1,f.b.vista().movimientos().size());}}
}
