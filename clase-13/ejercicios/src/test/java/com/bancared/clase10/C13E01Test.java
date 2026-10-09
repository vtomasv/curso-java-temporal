package com.bancared.clase10;
import org.junit.jupiter.api.*;
import java.util.*;
import static com.bancared.clase10.Modelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e01") class C13E01Test {
    static Transferencia t(String id,boolean vuelta,long monto,String reversa){return new Transferencia(id,id,vuelta?"PACIFICO":"CORDILLERA",vuelta?"B001":"A001",vuelta?"CORDILLERA":"PACIFICO",vuelta?"A001":"B001",monto,"Prueba","COMPLETADA","",reversa,false);}
    @Test void posicionesSeCompensanSinRedebitarClientes(){var n=CalculoNeto.calcular(List.of(t("a",false,100000,"SIN_SOLICITAR"),t("b",true,60000,"SIN_SOLICITAR")));assertEquals(-40000L,n.get("CORDILLERA"));assertEquals(40000L,n.get("PACIFICO"));}
    @Test void reversaCompletadaNeteaCero(){var n=CalculoNeto.calcular(List.of(t("a",false,100000,"COMPLETADA")));assertEquals(0L,n.get("CORDILLERA"));assertEquals(0L,n.get("PACIFICO"));}
}
