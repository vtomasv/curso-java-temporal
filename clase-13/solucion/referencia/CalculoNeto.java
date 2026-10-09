package com.bancared.clase10;
import java.util.*;
import static com.bancared.clase10.Modelos.*;
public final class CalculoNeto {
    private CalculoNeto(){}
    public static Map<String,Long> calcular(List<Transferencia> transferencias){
        Map<String,Long> netos=new LinkedHashMap<>();netos.put("CORDILLERA",0L);netos.put("PACIFICO",0L);
        for(var t:transferencias){if(!"COMPLETADA".equals(t.estado())||"COMPLETADA".equals(t.reversa()))continue;
            netos.put(t.bancoOrigen(),Math.subtractExact(netos.get(t.bancoOrigen()),t.monto()));netos.put(t.bancoDestino(),Math.addExact(netos.get(t.bancoDestino()),t.monto()));}
        if(Math.addExact(netos.get("CORDILLERA"),netos.get("PACIFICO"))!=0)throw new IllegalStateException("El neteo no conserva el total.");return Map.copyOf(netos);
    }
}
