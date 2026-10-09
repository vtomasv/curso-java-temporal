package com.bancared.clase10;
import java.util.*;
import static com.bancared.clase10.Modelos.*;
public final class CalculoNeto {
    private CalculoNeto(){}
    public static Map<String,Long> calcular(List<Transferencia> transferencias){
        // TODO(C13-E01): neto origen negativo/destino positivo. Original con reversa COMPLETADA aporta cero. Math.addExact y suma cero.
        return Map.of("CORDILLERA",0L,"PACIFICO",0L);
    }
}
