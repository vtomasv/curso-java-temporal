package com.sigeo.clase11;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.UpdateValidatorMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ApprovalWorkflow {

    @WorkflowMethod
    String processApproval(String requestId);

    @SignalMethod
    void approve(String commandId);

    @SignalMethod
    void reject(String commandId, String reason);

    @QueryMethod
    ApprovalState getState();

    @UpdateValidatorMethod
    void validateUpdatePriority(int newPriority);

    @UpdateMethod
    int updatePriority(int newPriority);
}
