package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class PortalService {
    private final TransferStore store; private final WorkflowClient client;
    public PortalService(TransferStore store,WorkflowClient client){this.store=store;this.client=client;}
    private WorkflowOptions options(String id){return WorkflowOptions.newBuilder().setTaskQueue(PortalConfiguration.TASK_QUEUE).setWorkflowId(id).setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_REJECT_DUPLICATE).build();}
    public Transferencia iniciar(SolicitudTransferencia s){
        Transferencia t=store.crear(s);
        TransferenciaWorkflow w=client.newWorkflowStub(TransferenciaWorkflow.class,options("transferencia-"+t.id()));
        try{WorkflowClient.start(w::transferir,t);}catch(WorkflowExecutionAlreadyStarted e){/* La clave estable recupera la misma ejecución. */}
        return store.obtener(t.id());
    }
    public Transferencia reversar(String id){
        if(!ReversaWorkflowImpl.disponible())throw new ErrorNegocio("REVERSA_NO_DISPONIBLE","Completa el laboratorio C10-E03 y reinicia la aplicación.");
        Transferencia t=store.obtener(id);
        if(!"COMPLETADA".equals(t.estado()) || t.liquidada())throw new ErrorNegocio("OPERACION_NO_ELEGIBLE","Solo se reversan transferencias completadas sin liquidar.");
        ReversaWorkflow w=client.newWorkflowStub(ReversaWorkflow.class,options("reversa-"+id));
        try{WorkflowClient.start(w::reversar,id);}catch(WorkflowExecutionAlreadyStarted e){/* Una sola reversa por operación original. */}
        return store.obtener(id);
    }
}
