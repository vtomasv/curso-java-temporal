package com.bancared.clase10;
import java.util.Map;
public final class CambiosAprobacion {
    private CambiosAprobacion() {}
    public static long aplicar(String clave,long nuevo,long actual,Map<String,Long> comandos){
        Long previo=comandos.get(clave);
        if(previo!=null){if(previo.longValue()!=nuevo)throw new ErrorNegocio("CLAVE_REUTILIZADA","El comando ya identifica otro monto.");return actual;}
        comandos.put(clave,nuevo);return nuevo;
    }
}
