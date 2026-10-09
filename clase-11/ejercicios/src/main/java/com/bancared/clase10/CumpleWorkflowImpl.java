package com.bancared.clase10;
import io.temporal.workflow.Workflow;
public class CumpleWorkflowImpl implements CumpleWorkflow {
    private final CelebracionesActivities actividades=Workflow.newActivityStub(CelebracionesActivities.class,PoliticaActivities.persistencia());
    public String ejecutar(String fecha){
        // TODO(C11-E02): fecha vacía solicita fechaChile por Activity; reemplazarSaludos y devolver fecha + " · saludos: " + cantidad.
        return "LAB_PENDIENTE";
    }
}
