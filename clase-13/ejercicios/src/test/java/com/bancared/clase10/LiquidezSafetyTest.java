package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static com.bancared.clase10.LiquidacionModelos.*;
import static org.junit.jupiter.api.Assertions.*;
class LiquidezSafetyTest {
    @Test void comandosIdempotentesYNoPermiteSaldoNegativo(){var s=new LiquidezStore(NuevosTestSupport.jdbc());var c=new OrdenLiquidez("l:debito","l",-100000);var r=s.mover(c);assertEquals(r,s.mover(c));assertEquals(1900000,s.vista().saldo());assertThrows(ErrorNegocio.class,()->s.mover(new OrdenLiquidez("l:debito","l",-200000)));assertThrows(ErrorNegocio.class,()->s.mover(new OrdenLiquidez("otro","l",-2000000)));assertEquals(1,s.vista().movimientos().size());}
}
