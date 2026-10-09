package com.bancared.clase10;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class PortalConfiguration {
    public static final String TASK_QUEUE="BANCA-CLASE15";
    @Bean TransferStore transferStore(JdbcTemplate jdbc){return new TransferStore(jdbc);}
    @Bean BankGateway bankGateway(@Value("${banking.cordillera-url}")String a,@Value("${banking.pacifico-url}")String b,@Value("${banking.service-password}")String p){return new BankGateway(a,b,p);}
    @Bean BankActivitiesImpl activities(BankGateway gateway,TransferStore store){return new BankActivitiesImpl(gateway,store);}
    @Bean(destroyMethod="close") MotorTemporal motor(@Value("${banking.temporal.mode}")String mode,@Value("${banking.temporal.target}")String target,BankActivitiesImpl activities,java.util.List<ExtraWorkerModule> modulos){return new MotorTemporal(mode,target,activities,modulos);}
    @Bean WorkflowClient workflowClient(MotorTemporal motor){return motor.client;}
    public static class MotorTemporal implements AutoCloseable {
        final WorkflowClient client; final WorkerFactory factory; final WorkflowServiceStubs servicio; final TestWorkflowEnvironment embebido;
        MotorTemporal(String modo,String target,BankActivitiesImpl activities,java.util.List<ExtraWorkerModule> modulos){
            if("embedded".equals(modo)){
                embebido=TestWorkflowEnvironment.newInstance();servicio=null;factory=null;client=embebido.getWorkflowClient();
                registrar(embebido.newWorker(TASK_QUEUE),activities,modulos);embebido.start();
            }else{
                if(!"external".equals(modo))throw new IllegalArgumentException("Modo Temporal desconocido.");
                embebido=null;servicio=WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder().setTarget(target).build());client=WorkflowClient.newInstance(servicio);factory=WorkerFactory.newInstance(client);registrar(factory.newWorker(TASK_QUEUE),activities,modulos);factory.start();
            }
        }
        private static void registrar(Worker worker,BankActivitiesImpl activities,java.util.List<ExtraWorkerModule> modulos){worker.registerWorkflowImplementationTypes(TransferenciaWorkflowImpl.class,ReversaWorkflowImpl.class);worker.registerActivitiesImplementations(activities);modulos.forEach(m->m.registrar(worker));}
        @Override public void close(){if(embebido!=null)embebido.close();else{factory.shutdown();servicio.shutdown();}}
    }
}
