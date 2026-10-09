package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
public final class SeleccionLote {
    private SeleccionLote(){}
    public static boolean elegible(Transferencia t){
        return "COMPLETADA".equals(t.estado())&&!t.liquidada()&&t.lote()==null&&java.util.Set.of("SIN_SOLICITAR","COMPLETADA","RECHAZADA").contains(t.reversa());
    }
}
