package com.bancared.clase10;
import static com.bancared.clase10.EventosModelos.*;
public final class PoliticaMensaje {
    private PoliticaMensaje(){}
    public static boolean valido(Evento e){
        return e!=null&&e.version()==1&&e.id()!=null&&e.id().matches("[A-Za-z0-9_:-]{1,120}")&&e.referencia()!=null&&e.referencia().matches("[A-Za-z0-9_-]{1,80}")&&e.tipo()!=null&&java.util.Set.of("TRANSFERENCIA_COMPLETADA","REVERSA_COMPLETADA","LOTE_LIQUIDADO").contains(e.tipo())&&e.monto()>=0&&e.monto()<=100_000_000L;
    }
}
