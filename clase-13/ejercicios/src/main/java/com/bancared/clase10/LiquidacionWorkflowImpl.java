package com.bancared.clase10;
import io.temporal.workflow.Workflow;
import io.temporal.failure.*;
import static com.bancared.clase10.LiquidacionModelos.*;
public class LiquidacionWorkflowImpl implements LiquidacionWorkflow {
    private final LiquidacionActivities banco=Workflow.newActivityStub(LiquidacionActivities.class,PoliticaActivities.operaciones());
    private final LiquidacionActivities registro=Workflow.newActivityStub(LiquidacionActivities.class,PoliticaActivities.persistencia());
    public Lote liquidar(String id){
        // TODO(C13-E03): snapshot, débito al banco deudor y crédito al acreedor con claves estables. Confirmar recibos; compensar solo rechazo definitivo conocido. Incierto queda PENDIENTE_REVISION.
        return registro.terminarLote(id,"LAB_PENDIENTE","Completa C13-E03");
    }
    private boolean definitivo(ActivityFailure f){return f.getCause() instanceof ApplicationFailure a&&a.isNonRetryable();}
}
