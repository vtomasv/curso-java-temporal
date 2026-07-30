package com.sigeo.clase12;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SagaWorkflowImpl implements SagaWorkflow {

    private String status = "STARTED";

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .setRetryOptions(RetryOptions.newBuilder()
                    .setMaximumAttempts(3)
                    .build())
            .build();

    private final SagaActivities activities = Workflow.newActivityStub(SagaActivities.class, options);

    @Override
    public String executeSaga(String reservationId, boolean failAtBudget, boolean failAtAgenda, boolean failAtNotification) {
        // TODO(C12-E02): Implementar dos pasos (reserva y presupuesto) y compensar el primero si falla el segundo.
        // Usar io.temporal.workflow.Saga para registrar las compensaciones.
        
        // TODO(C12-E03): Parametrizar fallo en cada paso y verificar estado final.
        // Usar los booleanos failAtBudget, failAtAgenda, failAtNotification para inducir fallos.
        
        // TODO(C12-E04): Hacer fallar liberación temporalmente y configurar retry diferente.
        // Configurar ActivityOptions específicos para las compensaciones con más reintentos.
        
        // TODO(C12-E07): Convertir reserva de recurso y presupuesto en child workflows.
        // Reemplazar las llamadas a activities por llamadas a ChildWorkflows.

        throw new UnsupportedOperationException("TODO C12-E02, C12-E03, C12-E04, C12-E07");
    }

    @Override
    public String getStatus() {
        return status;
    }
}
