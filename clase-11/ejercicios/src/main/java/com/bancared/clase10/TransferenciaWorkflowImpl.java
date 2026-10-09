package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Workflow;

/** Base preparada: dinero incierto queda pendiente, nunca se devuelve a ciegas. */
public class TransferenciaWorkflowImpl implements TransferenciaWorkflow {
    private final BankActivities banco=Workflow.newActivityStub(BankActivities.class,PoliticaActivities.operaciones());
    private final BankActivities registro=Workflow.newActivityStub(BankActivities.class,PoliticaActivities.persistencia());
    @Override public ResultadoWorkflow transferir(Transferencia t){
        ComandoBanco debito=new ComandoBanco(t.id()+":debito",t.id(),t.cuentaOrigen(),t.monto(),Direccion.DEBITO);
        ComandoBanco credito=new ComandoBanco(t.id()+":credito",t.id(),t.cuentaDestino(),t.monto(),Direccion.CREDITO);
        try{banco.ejecutar(t.bancoOrigen(),debito);}catch(ActivityFailure fallo){
            try{if(!banco.consultar(t.bancoOrigen(),debito.commandId()).aplicado())return registro.terminar(t.id(),rechazoDefinitivo(fallo)?"RECHAZADA":"PENDIENTE_REVISION","No hay confirmación del débito. El error determina si requiere revisión.");}
            catch(ActivityFailure sinConfirmacion){return registro.terminar(t.id(),"PENDIENTE_REVISION","No se conoce el resultado del débito.");}
        }
        try{banco.ejecutar(t.bancoDestino(),credito);}
        catch(ActivityFailure fallo){
            ConsultaComando confirmacion;
            try{confirmacion=banco.consultar(t.bancoDestino(),credito.commandId());}
            catch(ActivityFailure sinConfirmacion){return registro.terminar(t.id(),"PENDIENTE_REVISION","Débito aplicado. Resultado del crédito incierto. No se devuelve dinero automáticamente.");}
            if(confirmacion.aplicado())return registro.terminar(t.id(),"COMPLETADA","El banco confirmó el crédito que perdió su respuesta.");
            // Un timeout puede dejar una solicitud HTTP todavía en ejecución.
            // Un resultado ausente no autoriza devolver fondos ante un fallo transitorio.
            if(!rechazoDefinitivo(fallo))return registro.terminar(t.id(),"PENDIENTE_REVISION","No hay confirmación del crédito. Conservamos el débito para conciliación, sin compensación a ciegas.");
            ComandoBanco compensar=new ComandoBanco(t.id()+":compensar",t.id()+"-compensacion",t.cuentaOrigen(),t.monto(),Direccion.CREDITO);
            try{banco.ejecutar(t.bancoOrigen(),compensar);}
            catch(ActivityFailure falloCompensacion){
                try{if(!banco.consultar(t.bancoOrigen(),compensar.commandId()).aplicado())return registro.terminar(t.id(),"PENDIENTE_REVISION","Crédito ausente. Compensación pendiente.");}
                catch(ActivityFailure sinConfirmacion){return registro.terminar(t.id(),"PENDIENTE_REVISION","No se conoce el resultado de la compensación.");}
            }
            return registro.terminar(t.id(),"COMPENSADA","Crédito no aplicado. Débito compensado mediante un nuevo movimiento.");
        }
        return registro.terminar(t.id(),"COMPLETADA","Débito y crédito confirmados una sola vez.");
    }
    private boolean rechazoDefinitivo(ActivityFailure fallo){return fallo.getCause() instanceof ApplicationFailure a && a.isNonRetryable() && "RECHAZO_BANCO".equals(a.getType());}
}
