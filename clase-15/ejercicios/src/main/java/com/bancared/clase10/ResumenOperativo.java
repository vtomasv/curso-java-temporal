package com.bancared.clase10;
import java.util.List;
import static com.bancared.clase10.Modelos.*;
import static com.bancared.clase10.AsistenteModelos.*;
public final class ResumenOperativo {
    private ResumenOperativo(){}
    public static Resumen calcular(List<Transferencia> ts){
        // TODO(C15-E03): contar estados sin exportar cuentas/nombres. Volumen solo COMPLETADA, no confundir con saldo o neto. Sumar con Math.addExact.
        return new Resumen(ts.size(),0,0,0,0,0);
    }
}
