package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
public final class Idempotencia {
    private Idempotencia() {}
    public static ReciboBanco repetido(ComandoBanco pedido, ReciboBanco anterior) {
        // TODO(C10-E02): si coinciden transferId, cuentaId, monto y direccion,
        // devolver el recibo persistido. Si cambia alguno, lanzar CLAVE_REUTILIZADA.
        // La base rechaza el duplicado y conserva el saldo. El incremento permite
        // recuperar el resultado anterior cuando se perdió la respuesta HTTP.
        throw new ErrorNegocio("COMANDO_DUPLICADO", "La clave ya existe. Completa C10-E02 para recuperar su recibo.");
    }
}
