package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static com.bancared.clase10.EventosModelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e01") class C14E01Test {
    @Test void confirmacionAusenteNoPierdeEvento(){var s=new OutboxStore(NuevosTestSupport.jdbc());var e=new Evento("e1","TRANSFERENCIA_COMPLETADA","tx-a",100000,1);s.agregar(e);s.confirmar("e1",false);assertEquals(1,s.pendientes().size());s.confirmar("e1",true);assertEquals(0,s.pendientes().size());s.confirmar("e1",true);assertEquals(1,s.vista().size());}
}
