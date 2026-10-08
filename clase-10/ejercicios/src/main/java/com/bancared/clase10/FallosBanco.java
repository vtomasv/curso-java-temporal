package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
/** Fallos intencionales de laboratorio, fuera de cualquier Workflow. */
public class FallosBanco {
    private String modo="NORMAL"; private int restantes; private int latencia;
    public synchronized Escenario configurar(Escenario e) {
        if(e==null || !java.util.Set.of("NORMAL","TRANSITORIO","PERMANENTE","RESPUESTA_PERDIDA","LATENCIA").contains(e.modo()) || e.fallos()<0 || e.fallos()>5 || e.latenciaMs()<0 || e.latenciaMs()>5000)
            throw new ErrorNegocio("VALIDACION","Escenario inválido.");
        modo=e.modo();restantes=e.fallos();latencia=e.latenciaMs();return actual();
    }
    public synchronized Escenario actual(){return new Escenario(modo,restantes,latencia);}
    public void antes(ComandoBanco c) {
        if(c.direccion()!=Direccion.CREDITO || c.commandId().endsWith("compensar")) return;
        int espera=0;
        synchronized(this){
            if("PERMANENTE".equals(modo)) throw new ErrorNegocio("RECHAZO_BANCO","El banco rechazó el crédito de prueba.");
            if("TRANSITORIO".equals(modo) && restantes>0){restantes--;throw new ErrorNegocio("BANCO_NO_DISPONIBLE","503 simulado antes de aplicar el crédito.");}
            if("LATENCIA".equals(modo)) espera=latencia;
        }
        if(espera>0) try{Thread.sleep(espera);}catch(InterruptedException e){Thread.currentThread().interrupt();throw new ErrorNegocio("BANCO_NO_DISPONIBLE","Solicitud interrumpida.");}
    }
    public synchronized void despues(ComandoBanco c) {
        if(c.direccion()==Direccion.CREDITO && "RESPUESTA_PERDIDA".equals(modo) && restantes>0){restantes--;throw new ErrorNegocio("BANCO_NO_DISPONIBLE","Crédito aplicado. Se perdió la respuesta HTTP simulada.");}
    }
}
