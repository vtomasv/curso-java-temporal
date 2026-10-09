package com.bancared.clase10;
import io.temporal.workflow.Workflow;
import static com.bancared.clase10.AsistenteModelos.*;
public class AsistenteWorkflowImpl implements AsistenteWorkflow {
    private final AsistenteActivities actividades=Workflow.newActivityStub(AsistenteActivities.class,io.temporal.activity.ActivityOptions.newBuilder(PoliticaActivities.operaciones()).setStartToCloseTimeout(java.time.Duration.ofSeconds(12)).setScheduleToCloseTimeout(java.time.Duration.ofSeconds(40)).build());
    public Respuesta responder(Consulta c){Propuesta p=actividades.obtenerPropuesta(c);
        // TODO(C15-E01): Workflow.getVersion("validacion-respuesta", Workflow.DEFAULT_VERSION, 1). Historias v1 omiten validarPropuesta; nuevas la ejecutan.
        p=actividades.validarPropuesta(c.pregunta(),p);
        return actividades.guardarRespuesta(c,p);
    }
}
