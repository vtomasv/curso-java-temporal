package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static com.bancared.clase10.EventosModelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("base-c14-e03") class C14E03Test {
    @Test void venenoVaADlqSinEfecto(){var j=NuevosTestSupport.jdbc();var q=new ColaSql(j);var inbox=new InboxStore(j);q.veneno("veneno");assertEquals(1,q.consumir(25,inbox));assertTrue(inbox.todas().isEmpty());assertEquals("DLQ",q.vista().getFirst().get("ESTADO"));assertEquals(0,q.consumir(25,inbox));}
    @Test void duplicadoSeAckeaDespuesDeInbox(){var j=NuevosTestSupport.jdbc();var q=new ColaSql(j);var inbox=new InboxStore(j);var e=new Evento("e1","TRANSFERENCIA_COMPLETADA","tx-a",100000,1);q.publicar(e);q.publicar(e);assertEquals(2,q.consumir(25,inbox));assertEquals(1,inbox.todas().size());assertTrue(q.vista().stream().allMatch(r->"ACK".equals(r.get("ESTADO"))));}
}
