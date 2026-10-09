package com.bancared.clase10;
import org.junit.jupiter.api.*;
import static com.bancared.clase10.Modelos.*;
import static org.junit.jupiter.api.Assertions.*;
class OutboxSafetyTest {
    @Test void estadoYEventoSePersistenUnaVez(){var j=NuevosTestSupport.jdbc();var ts=new TransferStore(j);var t=ts.crear(new SolicitudTransferencia("a","CORDILLERA","A001","PACIFICO","B001",100000,"Prueba"));ts.terminar(t.id(),"COMPLETADA","");ts.terminar(t.id(),"COMPLETADA","");assertEquals(1,new OutboxStore(j).vista().size());}
}
