package com.bancared.clase10;
import java.util.*;
public final class LiquidacionModelos {
    private LiquidacionModelos(){}
    public record Lote(String id,String estado,Map<String,Long> netos,List<String> miembros,String detalle){}
    public record OrdenLiquidez(String clave,String lote,long delta){}
    public record ReciboLiquidez(String clave,String lote,long delta,long saldo){}
    public record Liquidez(long saldo,List<ReciboLiquidez> movimientos){}
}
