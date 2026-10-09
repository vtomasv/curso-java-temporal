package com.bancared.clase10;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e03") class C12E03Test {
    @Test void comandoViejoNoRevierteCambioPosterior(){var m=new HashMap<String,Long>();assertEquals(120000,CambiosAprobacion.aplicar("u1",120000,100000,m));assertEquals(150000,CambiosAprobacion.aplicar("u2",150000,120000,m));assertEquals(150000,CambiosAprobacion.aplicar("u1",120000,150000,m));assertThrows(ErrorNegocio.class,()->CambiosAprobacion.aplicar("u1",130000,150000,m));}
    @Test void updateConfirmadoAntesDeAprobar(){try(var f=new AprobacionFixture()){var w=f.iniciar();for(int n=0;n<50&&!w.estado().startsWith("ESPERANDO");n++)Thread.yield();assertEquals(120000,w.cambiarMonto("u1",120000));assertEquals(120000,w.cambiarMonto("u1",120000));w.decidir("d1","APROBAR");assertEquals("COMPLETADA",f.resultado(w).estado());assertEquals(120000,f.montoHijo);}}
}
