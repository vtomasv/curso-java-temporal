package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static com.bancared.clase10.Modelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("base-c13-e02") class C13E02Test {
    @Test void snapshotNoSeComparteEntreLotes(){var j=NuevosTestSupport.jdbc();var transfers=new TransferStore(j);var s=new CompensacionStore(j,transfers);var t=transfers.crear(new SolicitudTransferencia("a","CORDILLERA","A001","PACIFICO","B001",100000,"Prueba"));transfers.terminar(t.id(),"COMPLETADA","");var uno=s.preparar("l1");assertEquals(1,uno.miembros().size());assertEquals(uno,s.preparar("l1"));assertTrue(s.preparar("l2").miembros().isEmpty());assertThrows(ErrorNegocio.class,()->transfers.reservarReversa(t.id()));}
    @Test void excluyeReversaEnCurso(){assertFalse(SeleccionLote.elegible(C13E01Test.t("a",false,100000,"PENDIENTE_REVISION")));assertTrue(SeleccionLote.elegible(C13E01Test.t("a",false,100000,"SIN_SOLICITAR")));}
}
