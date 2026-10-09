package com.bancared.clase10;
public final class EventosModelos {
    private EventosModelos(){}
    public record Evento(String id,String tipo,String referencia,long monto,int version){}
    public record Notificacion(String id,String referencia,String texto){}
}
