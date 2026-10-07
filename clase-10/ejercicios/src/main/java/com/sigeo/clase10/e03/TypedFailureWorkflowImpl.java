package com.sigeo.clase10.e03;

import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class TypedFailureWorkflowImpl implements TypedFailureWorkflow {

    private final TypedFailureActivity activity = Workflow.newActivityStub(TypedFailureActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(2))
                    .build());

    @Override
    public String process(String data) {
        try {
            activity.validateData(data);
            return "Success";
        } catch (ActivityFailure e) {
            if (e.getCause() instanceof ApplicationFailure failure) {
                return switch (failure.getType()) {
                    case "VALIDATION" -> "Validation Error";
                    case "NOT_FOUND" -> "Not Found Error";
                    case "PROVIDER_UNAVAILABLE" -> "Provider Error";
                    default -> throw e;
                };
            }
            throw e;
        }
    }
}
