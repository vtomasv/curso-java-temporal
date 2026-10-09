package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;

final class WorkflowFixture implements AutoCloseable {
    final BankStore a=TestStores.bank("CORDILLERA"),b=TestStores.bank("PACIFICO");
    final TransferStore portal=TestStores.portal();
    final FallosBanco fallos=new FallosBanco();
    final Map<String,AtomicInteger> intentos=new ConcurrentHashMap<>();
    boolean consultaIncierta;
    final TestWorkflowEnvironment env=TestWorkflowEnvironment.newInstance();
    WorkflowFixture(){
        var worker=env.newWorker("TEST-BANCA");worker.registerWorkflowImplementationTypes(TransferenciaWorkflowImpl.class,ReversaWorkflowImpl.class);
        worker.registerActivitiesImplementations(new BankActivities(){
            public ReciboBanco ejecutar(String banco,ComandoBanco c){
                intentos.computeIfAbsent(c.commandId(),k->new AtomicInteger()).incrementAndGet();
                try{if("PACIFICO".equals(banco))fallos.antes(c);var r=bank(banco).ejecutar(c);if("PACIFICO".equals(banco))fallos.despues(c);return r;}
                catch(ErrorNegocio e){if("BANCO_NO_DISPONIBLE".equals(e.tipo()))throw ApplicationFailure.newFailure(e.getMessage(),e.tipo());throw ApplicationFailure.newNonRetryableFailure(e.getMessage(),"RECHAZO_BANCO");}
            }
            public ConsultaComando consultar(String banco,String id){if(consultaIncierta&&"PACIFICO".equals(banco))throw ApplicationFailure.newFailure("No hay confirmación","BANCO_NO_DISPONIBLE");return bank(banco).consultar(id);}
            public Transferencia transferencia(String id){return portal.obtener(id);}
            public ResultadoWorkflow terminar(String id,String estado,String detalle){return portal.terminar(id,estado,detalle);}
            public ResultadoWorkflow registrarReversa(String id,String estado,String detalle){return portal.reversa(id,estado,detalle);}
        });env.start();
    }
    BankStore bank(String codigo){return "CORDILLERA".equals(codigo)?a:b;}
    Transferencia crear(String clave,long monto){return portal.crear(new SolicitudTransferencia(clave,"CORDILLERA","A001","PACIFICO","B001",monto,"Prueba"));}
    ResultadoWorkflow transferir(Transferencia t){return env.getWorkflowClient().newWorkflowStub(TransferenciaWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue("TEST-BANCA").setWorkflowId("transferencia-"+t.id()).build()).transferir(t);}
    ResultadoWorkflow reversar(String id){return env.getWorkflowClient().newWorkflowStub(ReversaWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue("TEST-BANCA").setWorkflowId("reversa-"+id+"-"+UUID.randomUUID()).build()).reversar(id);}
    long saldoA(){return a.cuentas().getFirst().saldo();}long saldoB(){return b.cuentas().getFirst().saldo();}
    @Override public void close(){env.close();}
}
