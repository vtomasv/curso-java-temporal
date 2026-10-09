package com.bancared.clase10;
import static com.bancared.clase10.EventosModelos.*;
public final class PoliticaMensaje {
    private PoliticaMensaje(){}
    public static boolean valido(Evento e){
        // TODO(C14-E03): contrato v1, id/referencia no vacíos, tipos permitidos y monto no negativo. Mensaje inválido va a DLQ, nunca a retry infinito.
        return true;
    }
}
