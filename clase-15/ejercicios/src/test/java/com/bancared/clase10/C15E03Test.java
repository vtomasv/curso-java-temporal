package com.bancared.clase10;
import org.junit.jupiter.api.*;
import java.util.List;
import static com.bancared.clase10.Modelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e03") class C15E03Test {
    @Test void volumenNoIncluyeRechazosNiDatosPersonales(){var a=C13E01Test.t("a",false,100000,"COMPLETADA");var b=new Transferencia("b","b","CORDILLERA","A001","PACIFICO","B001",60000,"Prueba","PENDIENTE_REVISION","","SIN_SOLICITAR",false);var r=ResumenOperativo.calcular(List.of(a,b));assertEquals(2,r.transferencias());assertEquals(1,r.completadas());assertEquals(1,r.pendientes());assertEquals(100000,r.volumenCompletado());assertEquals(1,r.reversadas());assertFalse(r.toString().contains("A001"));}
}
