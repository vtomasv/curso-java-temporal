package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
public final class Idempotencia {
    private Idempotencia() {}
    public static ReciboBanco repetido(ComandoBanco pedido,ReciboBanco anterior){
        if(!pedido.transferId().equals(anterior.transferId()) || !pedido.cuentaId().equals(anterior.cuentaId()) || pedido.monto()!=anterior.monto() || pedido.direccion()!=anterior.direccion())
            throw new ErrorNegocio("CLAVE_REUTILIZADA","La misma clave identifica un comando diferente.");
        return anterior;
    }
}
