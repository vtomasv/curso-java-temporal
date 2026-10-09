package com.bancared.clase10;
import java.util.List;
public final class Modelos {
    private Modelos() {}
    public enum Direccion { DEBITO, CREDITO }
    public record Cuenta(String id, String titular, long saldo, String nacimiento) {}
    public record ComandoBanco(String commandId, String transferId, String cuentaId, long monto, Direccion direccion) {}
    public record ReciboBanco(String commandId, String transferId, String cuentaId, long monto, Direccion direccion, long saldoResultante) {}
    public record ConsultaComando(boolean aplicado, ReciboBanco recibo) {}
    public record SolicitudTransferencia(String commandId, String bancoOrigen, String cuentaOrigen, String bancoDestino, String cuentaDestino, long monto, String descripcion) {}
    public record Transferencia(String id, String commandId, String bancoOrigen, String cuentaOrigen, String bancoDestino, String cuentaDestino, long monto, String descripcion, String estado, String detalle, String reversa, boolean liquidada,String lote) {
        public Transferencia(String id,String commandId,String bancoOrigen,String cuentaOrigen,String bancoDestino,String cuentaDestino,long monto,String descripcion,String estado,String detalle,String reversa,boolean liquidada){this(id,commandId,bancoOrigen,cuentaOrigen,bancoDestino,cuentaDestino,monto,descripcion,estado,detalle,reversa,liquidada,null);}
    }
    public record ResultadoWorkflow(String transferId, String estado, String detalle) {}
    public record Escenario(String modo, int fallos, int latenciaMs) {}
    public record Intento(String transferId, String etapa, int intento, String resultado) {}
    public record VistaBanco(String codigo, boolean disponible, List<Cuenta> cuentas, List<ReciboBanco> movimientos) {}
}
