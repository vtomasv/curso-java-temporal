package com.bancared.clase10;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import static com.bancared.clase10.LiquidacionModelos.*;
import static com.bancared.clase10.Modelos.*;
class LiquidacionFixture implements AutoCloseable {
    final TestWorkflowEnvironment env=TestWorkflowEnvironment.newInstance();final LiquidezStore a=new LiquidezStore(NuevosTestSupport.jdbc()),b=new LiquidezStore(NuevosTestSupport.jdbc());final TransferStore ts;final CompensacionStore s;String fallo="";
    LiquidacionFixture(){var j=NuevosTestSupport.jdbc();ts=new TransferStore(j);s=new CompensacionStore(j,ts);var t=ts.crear(new SolicitudTransferencia("a","CORDILLERA","A001","PACIFICO","B001",100000,"Prueba"));ts.terminar(t.id(),"COMPLETADA","");s.preparar("lote");var w=env.newWorker("C13");w.registerWorkflowImplementationTypes(LiquidacionWorkflowImpl.class);w.registerActivitiesImplementations(new LiquidacionActivities(){public Lote prepararLote(String id){return s.preparar(id);}public Lote terminarLote(String id,String e,String d){return s.terminar(id,e,d);}public ReciboLiquidez moverLiquidez(String banco,OrdenLiquidez c){if("PACIFICO".equals(banco)){if("RECHAZO".equals(fallo))throw ApplicationFailure.newNonRetryableFailure("Rechazo","RECHAZO_BANCO");if("INCIERTO".equals(fallo))throw ApplicationFailure.newFailure("Timeout","BANCO_NO_DISPONIBLE");if("RESPUESTA_PERDIDA".equals(fallo)){b.mover(c);throw ApplicationFailure.newFailure("Respuesta perdida","BANCO_NO_DISPONIBLE");}}return ("CORDILLERA".equals(banco)?a:b).mover(c);}public boolean confirmarLiquidez(String banco,String clave){return ("CORDILLERA".equals(banco)?a:b).consultar(clave)!=null;}});env.start();}
    Lote ejecutar(){return env.getWorkflowClient().newWorkflowStub(LiquidacionWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue("C13").build()).liquidar("lote");}
    public void close(){env.close();}
}
