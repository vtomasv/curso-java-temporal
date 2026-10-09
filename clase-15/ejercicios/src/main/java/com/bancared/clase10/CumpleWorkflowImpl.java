package com.bancared.clase10;
import io.temporal.workflow.Workflow;
public class CumpleWorkflowImpl implements CumpleWorkflow {
    private final CelebracionesActivities actividades=Workflow.newActivityStub(CelebracionesActivities.class,PoliticaActivities.persistencia());
    public String ejecutar(String fecha){
        String dia=fecha==null||fecha.isBlank()?actividades.fechaChile():java.time.LocalDate.parse(fecha).toString();
        int cantidad=actividades.reemplazarSaludos(dia);
        return dia+" · saludos: "+cantidad;
    }
}
