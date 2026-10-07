package com.sigeo.clase10.e03;

import io.temporal.failure.ApplicationFailure;

public class TypedFailureActivityImpl implements TypedFailureActivity {
    @Override
    public void validateData(String data) {
        switch (data) {
            case "invalid" -> throw ApplicationFailure.newNonRetryableFailure(
                    "Validation failed",
                    "VALIDATION"
            );
            case "missing" -> throw ApplicationFailure.newNonRetryableFailure(
                    "Data not found",
                    "NOT_FOUND"
            );
            case "down" -> throw ApplicationFailure.newNonRetryableFailure(
                    "Provider is down",
                    "PROVIDER_UNAVAILABLE"
            );
            default -> {
                // Datos válidos: la Activity termina normalmente.
            }
        }
    }
}
