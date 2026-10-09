package com.bancared.clase10;
import io.temporal.workflow.Workflow;
import io.temporal.failure.*;
import static com.bancared.clase10.LiquidacionModelos.*;
public class LiquidacionWorkflowImpl implements LiquidacionWorkflow {
    private final LiquidacionActivities banco=Workflow.newActivityStub(LiquidacionActivities.class,PoliticaActivities.operaciones());
    private final LiquidacionActivities registro=Workflow.newActivityStub(LiquidacionActivities.class,PoliticaActivities.persistencia());
    public Lote liquidar(String id){
        Lote l=registro.prepararLote(id);
        if(!"PREPARADO".equals(l.estado()))return l;
        long neto=l.netos().get("CORDILLERA");if(neto==0)return registro.terminarLote(id,"LIQUIDADO","Neto cero; no se mueve liquidez.");
        String deudor=neto<0?"CORDILLERA":"PACIFICO",acreedor=neto<0?"PACIFICO":"CORDILLERA";long monto=Math.abs(neto);
        OrdenLiquidez debito=new OrdenLiquidez(id+":debito",id,-monto),credito=new OrdenLiquidez(id+":credito",id,monto);
        try{banco.moverLiquidez(deudor,debito);}catch(ActivityFailure f){
            try{if(!banco.confirmarLiquidez(deudor,debito.clave()))return registro.terminarLote(id,definitivo(f)?"RECHAZADO":"PENDIENTE_REVISION","Débito no confirmado.");}
            catch(ActivityFailure consulta){return registro.terminarLote(id,"PENDIENTE_REVISION","Débito incierto; lote reservado.");}
        }
        try{banco.moverLiquidez(acreedor,credito);}catch(ActivityFailure f){
            boolean aplicado;try{aplicado=banco.confirmarLiquidez(acreedor,credito.clave());}catch(ActivityFailure consulta){return registro.terminarLote(id,"PENDIENTE_REVISION","Crédito incierto; no se devuelve liquidez.");}
            if(aplicado)return registro.terminarLote(id,"LIQUIDADO","Crédito confirmado tras perder respuesta.");
            if(!definitivo(f))return registro.terminarLote(id,"PENDIENTE_REVISION","Crédito sin confirmación; no compensar a ciegas.");
            OrdenLiquidez compensar=new OrdenLiquidez(id+":compensar",id,monto);
            try{banco.moverLiquidez(deudor,compensar);}catch(ActivityFailure falloCompensar){try{if(!banco.confirmarLiquidez(deudor,compensar.clave()))return registro.terminarLote(id,"PENDIENTE_REVISION","Compensación pendiente.");}catch(ActivityFailure consulta){return registro.terminarLote(id,"PENDIENTE_REVISION","Compensación incierta.");}}
            return registro.terminarLote(id,"COMPENSADO","Rechazo definitivo del acreedor; liquidez devuelta con asiento nuevo.");
        }
        return registro.terminarLote(id,"LIQUIDADO","Ambos bancos confirmaron. Los saldos de clientes permanecen iguales.");
    }
    private boolean definitivo(ActivityFailure f){return f.getCause() instanceof ApplicationFailure a&&a.isNonRetryable();}
}
