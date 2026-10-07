package com.sigeo.clase10.e06;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;

public class ExportActivityImpl implements ExportActivity {

    private volatile boolean cleanupCalled = false;

    @Override
    public void exportData() {
        ActivityExecutionContext context = Activity.getExecutionContext();
        
        try {
            for (int i = 0; i < 100; i++) {
                context.heartbeat(i);
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Temporal lanza InterruptedException cuando se cancela la actividad
                    // si está bloqueada en sleep.
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted", e);
                }
            }
        } finally {
            cleanupCalled = true;
        }
    }

    public boolean isCleanupCalled() {
        return cleanupCalled;
    }
}
