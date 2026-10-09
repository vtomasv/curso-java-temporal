package com.bancared.clase10;
import org.junit.jupiter.api.*;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.client.WorkflowOptions;
import static org.junit.jupiter.api.Assertions.*;
@Tag("base-c11-e02") class C11E02Test {
    private String ejecutar(String fecha){try(var env=TestWorkflowEnvironment.newInstance()){
        var store=new CelebracionesStore(NuevosTestSupport.jdbc());var worker=env.newWorker("C11");worker.registerWorkflowImplementationTypes(CumpleWorkflowImpl.class);
        worker.registerActivitiesImplementations(new CelebracionesActivities(){public String fechaChile(){return "2026-10-08";}public int reemplazarSaludos(String f){return store.reemplazar(f,f.endsWith("10-08")?java.util.List.of("A001"):java.util.List.of());}});env.start();
        var w=env.getWorkflowClient().newWorkflowStub(CumpleWorkflow.class,WorkflowOptions.newBuilder().setTaskQueue("C11").build());String r=w.ejecutar(fecha);assertEquals(fecha.isBlank()?"2026-10-08":fecha,store.vista().get("fecha"));return r;
    }}
    @Test void fechaExplicita(){assertEquals("2026-10-08 · saludos: 1",ejecutar("2026-10-08"));}
    @Test void fechaDelDiaSeObtieneFueraDelWorkflow(){assertEquals("2026-10-08 · saludos: 1",ejecutar(""));}
}
