package com.bancared.clase10;
import java.util.List;
import static com.bancared.clase10.Modelos.*;
import static com.bancared.clase10.AsistenteModelos.*;
public final class ResumenOperativo {
    private ResumenOperativo(){}
    public static Resumen calcular(List<Transferencia> ts){
        int completas=0,pendientes=0,liquidadas=0,reversadas=0;long volumen=0;
        for(var t:ts){if("COMPLETADA".equals(t.estado())){completas++;volumen=Math.addExact(volumen,t.monto());}if(t.estado().startsWith("PENDIENTE"))pendientes++;if(t.liquidada())liquidadas++;if("COMPLETADA".equals(t.reversa()))reversadas++;}
        return new Resumen(ts.size(),completas,pendientes,volumen,liquidadas,reversadas);
    }
}
