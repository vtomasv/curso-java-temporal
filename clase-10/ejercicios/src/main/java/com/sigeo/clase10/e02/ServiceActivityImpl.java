package com.sigeo.clase10.e02;

import io.temporal.activity.Activity;
import io.temporal.failure.ApplicationFailure;

public class ServiceActivityImpl implements ServiceActivity {

    @Override
    public String processRequest(String input) {
        int attempt = Activity.getExecutionContext().getInfo().getAttempt();
        if ("503".equals(input) && attempt < 3) {
            throw ApplicationFailure.newFailure("Service Unavailable", "503");
        }
        if ("400".equals(input)) {
            throw ApplicationFailure.newFailure("Bad Request", "400");
        }
        return "Processed: " + input;
    }
}
