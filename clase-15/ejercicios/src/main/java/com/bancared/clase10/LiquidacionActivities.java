package com.bancared.clase10;
import io.temporal.activity.ActivityInterface;
import static com.bancared.clase10.LiquidacionModelos.*;
@ActivityInterface public interface LiquidacionActivities {Lote prepararLote(String id);Lote terminarLote(String id,String estado,String detalle);ReciboLiquidez moverLiquidez(String banco,OrdenLiquidez orden);boolean confirmarLiquidez(String banco,String clave);}
