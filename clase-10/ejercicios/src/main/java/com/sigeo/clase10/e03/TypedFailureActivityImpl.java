package com.sigeo.clase10.e03;

import io.temporal.failure.ApplicationFailure;

public class TypedFailureActivityImpl implements TypedFailureActivity {
    @Override
    public void validateData(String data) {
        // TODO(C10-E03): Lanza un ApplicationFailure con tipo "VALIDATION" si data es "invalid"
        // Lanza un ApplicationFailure con tipo "NOT_FOUND" si data es "missing"
        // Lanza un ApplicationFailure con tipo "PROVIDER_UNAVAILABLE" si data es "down"
        throw new UnsupportedOperationException("TODO C10-E03");
    }
}
