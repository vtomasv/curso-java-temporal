package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.client.*;
import java.util.concurrent.atomic.AtomicInteger;
class AprobacionFixture implements AutoCloseable {
    final TestWorkflowEnvironment env=TestWorkflowEnvironment.newInstance();final AtomicInteger preparaciones=new AtomicInteger();long montoHijo;
    AprobacionFixture(){var w=env.newWorker("C12");w.registerWorkflowImplementationTypes(AprobacionWorkflowImpl.class,Hijo.class);w.registerActivitiesImplementations(new AprobacionActivities(){public ResultadoWorkflow estadoSolicitud(String k,String e,String d){return new ResultadoWorkflow("tx-"+k,e,d);}public Transferencia prepararTransferencia(SolicitudTransferencia r){preparaciones.incrementAndGet();montoHijo=r.monto();return new Transferencia("tx-"+r.commandId(),r.commandId(),r.bancoOrigen(),r.cuentaOrigen(),r.bancoDestino(),r.cuentaDestino(),r.monto(),r.descripcion(),"INICIADA","","SIN_SOLICITAR",false);}});env.start();}
    public static class Hijo implements TransferenciaWorkflow {public ResultadoWorkflow transferir(Transferencia t){return new ResultadoWorkflow(t.id(),"COMPLETADA","Hijo terminado");}}
    AprobacionWorkflow iniciar(){var w=env.getWorkflowClient().newWorkflowStub(AprobacionWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue("C12").build());WorkflowClient.start(w::iniciar,new SolicitudTransferencia("prueba","CORDILLERA","A001","PACIFICO","B001",100000,"Test"),30);return w;}
    ResultadoWorkflow resultado(AprobacionWorkflow w){return WorkflowStub.fromTyped(w).getResult(ResultadoWorkflow.class);}
    public void close(){env.close();}
}
