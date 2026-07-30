package com.sigeo.clase11;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface TechnicalReviewWorkflow {

    @WorkflowMethod
    String performReview(String requestId);
}
