package com.bancared.clase10;
import org.junit.jupiter.api.*;
import io.temporal.testing.*;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import static com.bancared.clase10.AsistenteModelos.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("lab-e01") class C15E01Test {
    public static class VersionUno implements AsistenteWorkflow {
        private final AsistenteActivities a=Workflow.newActivityStub(AsistenteActivities.class,io.temporal.activity.ActivityOptions.newBuilder(PoliticaActivities.operaciones()).setStartToCloseTimeout(java.time.Duration.ofSeconds(12)).setScheduleToCloseTimeout(java.time.Duration.ofSeconds(40)).build());
        public Respuesta responder(Consulta c){return a.guardarRespuesta(c,a.obtenerPropuesta(c));}
    }
    @Test void historiaRealV1SeReproduceConCodigoNuevo()throws Exception{try(var env=TestWorkflowEnvironment.newInstance()){
        var worker=env.newWorker("C15");worker.registerWorkflowImplementationTypes(VersionUno.class);worker.registerActivitiesImplementations(new AsistenteActivities(){public Propuesta obtenerPropuesta(Consulta c){return new Propuesta("Respuesta v1",java.util.List.of("POL-REVERSA"),"MODELO_SIMULADO");}public Propuesta validarPropuesta(String p,Propuesta r){return r;}public Respuesta guardarRespuesta(Consulta c,Propuesta r){return new Respuesta(c.clave(),r.respuesta(),r.fuentes(),"RESPONDIDA",r.proveedor());}});env.start();
        var w=env.getWorkflowClient().newWorkflowStub(AsistenteWorkflow.class,WorkflowOptions.newBuilder().setWorkflowId("historia-v1").setTaskQueue("C15").build());w.responder(new Consulta("q1","¿Puedo reversar?","VALIDA"));
        var historia=env.getWorkflowClient().fetchHistory("historia-v1");java.nio.file.Files.writeString(java.nio.file.Path.of("target/historia-v1.json"),historia.toJson(true));
        WorkflowReplayer.replayWorkflowExecution(historia,AsistenteWorkflowImpl.class);
    }}
}
