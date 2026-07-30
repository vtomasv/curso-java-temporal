package com.sigeo.clase12;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.QueryMethod;

@WorkflowInterface
public interface SagaWorkflow {

    @WorkflowMethod
    String executeSaga(String reservationId, boolean failAtBudget, boolean failAtAgenda, boolean failAtNotification);

    @QueryMethod
    String getStatus();
}
