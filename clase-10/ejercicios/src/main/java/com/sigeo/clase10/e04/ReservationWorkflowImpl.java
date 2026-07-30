package com.sigeo.clase10.e04;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class ReservationWorkflowImpl implements ReservationWorkflow {

    private final ReservationActivity activity = Workflow.newActivityStub(ReservationActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(5))
                    .build());

    @Override
    public String processReservation(String itemId) {
        // Usamos el Workflow ID como idempotency key
        String idempotencyKey = Workflow.getInfo().getWorkflowId();
        
        // Simulamos que la actividad se llama dos veces (ej. por un retry o un bug)
        String result1 = activity.makeReservation(itemId, idempotencyKey);
        String result2 = activity.makeReservation(itemId, idempotencyKey);
        
        return result2;
    }
}
