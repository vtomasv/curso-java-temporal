package com.sigeo.clase10.e05;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;

public class BatchProcessingActivityImpl implements BatchProcessingActivity {

    private boolean simulateCrash = true;

    @Override
    public int processBatch(int totalRecords) {
        ActivityExecutionContext context = Activity.getExecutionContext();

        int startOffset = context.getHeartbeatDetails(Integer.class).orElse(0);
        
        int processed = startOffset;
        
        for (int i = startOffset; i < totalRecords; i++) {
            // Simular procesamiento
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            
            processed++;
            
            if (processed % 100 == 0) {
                context.heartbeat(processed);
            }
            
            // Simulamos un crash a la mitad del procesamiento en el primer intento
            if (simulateCrash && processed == 500) {
                simulateCrash = false;
                throw new RuntimeException("Simulated crash at 500");
            }
        }
        
        return processed;
    }
}
