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
            // TODO(C10-E03): Captura el ActivityFailure, extrae el ApplicationFailure (e.getCause())
            // y retorna un string diferente según el tipo de error:
            // "VALIDATION" -> "Validation Error"
            // "NOT_FOUND" -> "Not Found Error"
            // "PROVIDER_UNAVAILABLE" -> "Provider Error"
            // Si es otro tipo, relanza la excepción.
            throw new UnsupportedOperationException("TODO C10-E03");
        }
    }
}
