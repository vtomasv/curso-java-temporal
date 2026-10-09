package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e01") class C12E01Test {
    @Test void limitaMontoYTimer(){for(long m:new long[]{0,-1,2_000_001})assertThrows(ErrorNegocio.class,()->PoliticaAprobacion.validar(m,30));for(int p:new int[]{4,301})assertThrows(ErrorNegocio.class,()->PoliticaAprobacion.validar(100,p));assertDoesNotThrow(()->PoliticaAprobacion.validar(2_000_000,300));}
}
