package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Workflow;

public class ReversaWorkflowImpl implements ReversaWorkflow {
    public static boolean disponible(){return true;}
    private final BankActivities banco=Workflow.newActivityStub(BankActivities.class,PoliticaActivities.operaciones());
    private final BankActivities registro=Workflow.newActivityStub(BankActivities.class,PoliticaActivities.persistencia());
    @Override public ResultadoWorkflow reversar(String id){
        Transferencia t=registro.transferencia(id);
        if("COMPLETADA".equals(t.reversa()))return new ResultadoWorkflow(id,"COMPLETADA","La reversa ya está aplicada.");
        if(!"COMPLETADA".equals(t.estado()) || t.liquidada() || t.lote()!=null)return registro.registrarReversa(id,"RECHAZADA","La operación original no es elegible.");
        String reversa=id+"-reversa";
        ComandoBanco debito=new ComandoBanco(id+":reversa-debito",reversa,t.cuentaDestino(),t.monto(),Direccion.DEBITO);
        ComandoBanco credito=new ComandoBanco(id+":reversa-credito",reversa,t.cuentaOrigen(),t.monto(),Direccion.CREDITO);
        try{banco.ejecutar(t.bancoDestino(),debito);}
        catch(ActivityFailure fallo){
            try{if(!banco.consultar(t.bancoDestino(),debito.commandId()).aplicado())return registro.registrarReversa(id,rechazoDefinitivo(fallo)?"RECHAZADA":"PENDIENTE_REVISION","El receptor no confirmó el débito de la reversa.");}
            catch(ActivityFailure sinConfirmacion){return registro.registrarReversa(id,"PENDIENTE_REVISION","Resultado del débito de reversa incierto.");}
        }
        try{banco.ejecutar(t.bancoOrigen(),credito);}
        catch(ActivityFailure fallo){
            ConsultaComando confirmado;
            try{confirmado=banco.consultar(t.bancoOrigen(),credito.commandId());}
            catch(ActivityFailure sinConfirmacion){return registro.registrarReversa(id,"PENDIENTE_REVISION","Débito de reversa aplicado. Crédito de reversa incierto.");}
            if(confirmado.aplicado())return registro.registrarReversa(id,"COMPLETADA","Crédito inverso confirmado tras perder la respuesta.");
            if(!rechazoDefinitivo(fallo))return registro.registrarReversa(id,"PENDIENTE_REVISION","El crédito inverso requiere conciliación. No se compensa a ciegas.");
            ComandoBanco compensar=new ComandoBanco(id+":reversa-compensar",id+"-reversa-compensacion",t.cuentaDestino(),t.monto(),Direccion.CREDITO);
            try{banco.ejecutar(t.bancoDestino(),compensar);}
            catch(ActivityFailure falloCompensacion){
                try{if(!banco.consultar(t.bancoDestino(),compensar.commandId()).aplicado())return registro.registrarReversa(id,"PENDIENTE_REVISION","Compensación de reversa pendiente.");}
                catch(ActivityFailure sinConfirmacion){return registro.registrarReversa(id,"PENDIENTE_REVISION","Compensación de reversa incierta.");}
            }
            return registro.registrarReversa(id,"RECHAZADA","Reversa rechazada. Débito inverso compensado.");
        }
        return registro.registrarReversa(id,"COMPLETADA","Reversa aplicada con nuevos movimientos. Transferencia original conservada.");
    }
    private boolean rechazoDefinitivo(ActivityFailure e){return e.getCause() instanceof ApplicationFailure a && a.isNonRetryable() && "RECHAZO_BANCO".equals(a.getType());}
}
