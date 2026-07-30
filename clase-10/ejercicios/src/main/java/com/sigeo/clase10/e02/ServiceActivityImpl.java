package com.sigeo.clase10.e02;

import io.temporal.failure.ApplicationFailure;

public class ServiceActivityImpl implements ServiceActivity {
    private int attempt = 0;

    @Override
    public String processRequest(String input) {
        attempt++;
        if ("503".equals(input) && attempt < 3) {
            throw ApplicationFailure.newFailure("Service Unavailable", "503");
        }
        if ("400".equals(input)) {
            throw ApplicationFailure.newFailure("Bad Request", "400");
        }
        return "Processed: " + input;
    }
}
