package com.bancared.clase10;
import java.util.Map;
public final class CambiosAprobacion {
    private CambiosAprobacion() {}
    public static long aplicar(String clave,long nuevo,long actual,Map<String,Long> comandos){
        // TODO(C12-E03): misma clave/monto devuelve actual, otro payload rechaza CLAVE_REUTILIZADA. Guardar nuevo comando y monto.
        throw new ErrorNegocio("LAB_PENDIENTE","Completa C12-E03 para cambiar el monto confirmado.");
    }
}
