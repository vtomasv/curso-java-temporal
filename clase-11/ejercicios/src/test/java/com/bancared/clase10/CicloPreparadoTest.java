package com.bancared.clase10;
import org.junit.jupiter.api.*;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.client.*;
import io.temporal.workflow.Workflow;
import io.temporal.api.enums.v1.EventType;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class CicloPreparadoTest {
    public static class NochePreparada implements CumpleWorkflow {
        public String ejecutar(String fecha){Workflow.newActivityStub(CelebracionesActivities.class,PoliticaActivities.persistencia()).reemplazarSaludos(fecha);return fecha;}
    }
    @Test void treintaYUnaNochesConTimerContinuanEnOtraHistory(){try(var env=TestWorkflowEnvironment.newInstance()){
        var fechas=Collections.synchronizedList(new ArrayList<String>());
        var worker=env.newWorker("CICLO");worker.registerWorkflowImplementationTypes(CicloCumpleWorkflowImpl.class,NochePreparada.class);
        worker.registerActivitiesImplementations(new CelebracionesActivities(){public String fechaChile(){return "2026-10-08";}public int reemplazarSaludos(String f){fechas.add(f);return 0;}});env.start();
        var w=env.getWorkflowClient().newWorkflowStub(CicloCumpleWorkflow.class,WorkflowOptions.newBuilder().setWorkflowId("ciclo-31").setTaskQueue("CICLO").build());
        var primera=WorkflowClient.start(w::simular,"2026-10-08",31,1,0);
        assertEquals("Noches procesadas: 31",WorkflowStub.fromTyped(w).getResult(String.class));
        assertEquals(31,fechas.size());assertEquals("2026-10-08",fechas.getFirst());assertEquals("2026-11-07",fechas.getLast());
        var historia=env.getWorkflowClient().fetchHistory(primera.getWorkflowId(),primera.getRunId());
        assertTrue(historia.getEvents().stream().anyMatch(e->e.getEventType()==EventType.EVENT_TYPE_WORKFLOW_EXECUTION_CONTINUED_AS_NEW));
    }}
}
