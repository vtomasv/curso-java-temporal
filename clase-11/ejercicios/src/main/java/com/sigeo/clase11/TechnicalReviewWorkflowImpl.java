package com.sigeo.clase11;

import io.temporal.workflow.Workflow;
import java.time.Duration;

public class TechnicalReviewWorkflowImpl implements TechnicalReviewWorkflow {

    @Override
    public String performReview(String requestId) {
        // Simulación de revisión técnica
        Workflow.sleep(Duration.ofMinutes(5));
        return "REVIEW_PASSED";
    }
}
