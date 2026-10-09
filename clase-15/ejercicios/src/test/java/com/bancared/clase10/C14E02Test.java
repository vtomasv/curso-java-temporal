package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static com.bancared.clase10.EventosModelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("base-c14-e02") class C14E02Test {
    @Test void redeliveryYReinicioNoDuplicanNotificacion(){var j=NuevosTestSupport.jdbc();var e=new Evento("e1","TRANSFERENCIA_COMPLETADA","tx-a",100000,1);var s=new InboxStore(j);var r=s.recibir(e);assertEquals(r,new InboxStore(j).recibir(e));assertEquals(1,s.todas().size());assertThrows(ErrorNegocio.class,()->s.recibir(new Evento("e1",e.tipo(),e.referencia(),200000,1)));}
}
