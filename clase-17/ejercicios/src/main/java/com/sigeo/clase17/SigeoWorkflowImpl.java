package com.sigeo.clase17;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class SigeoWorkflowImpl implements SigeoWorkflow {

    // TODO(C17-E02): Implementar la ruta crítica de la saga
    // TODO(C17-E03): Configurar retries y compensaciones para fallos de proveedor
    
    private final SigeoActivities activities = Workflow.newActivityStub(
            SigeoActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    @Override
    public void processRequest(String requestId) {
        throw new UnsupportedOperationException("TODO C17-E02: Implementar ruta crítica");
    }
}
