package com.sigeo.clase10.e01;

public class HttpActivityImpl implements HttpActivity {
    @Override
    public String callExternalService(int latencySeconds) {
        try {
            Thread.sleep(latencySeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", e);
        }
        return "Success";
    }
}
