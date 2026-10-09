package com.bancared.clase10;
import io.temporal.workflow.Workflow;
public class EventosWorkflowImpl implements EventosWorkflow {
    private final EventosActivities actividades=Workflow.newActivityStub(EventosActivities.class,io.temporal.activity.ActivityOptions.newBuilder(PoliticaActivities.operaciones()).setStartToCloseTimeout(java.time.Duration.ofSeconds(30)).setScheduleToCloseTimeout(java.time.Duration.ofSeconds(60)).build());
    public String procesar(String accion){if("publicar".equals(accion)){int n=0;for(var e:actividades.pendientes()){actividades.publicarEvento(e);n++;}return "Eventos enviados: "+n;}if("consumir".equals(accion))return "Mensajes procesados: "+actividades.consumirMensajes(25);throw new IllegalArgumentException("Acción de eventos inválida.");}
}
