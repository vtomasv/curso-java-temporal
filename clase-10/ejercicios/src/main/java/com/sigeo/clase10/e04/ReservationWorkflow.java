package com.sigeo.clase10.e04;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ReservationWorkflow {
    @WorkflowMethod
    String processReservation(String itemId);
}
