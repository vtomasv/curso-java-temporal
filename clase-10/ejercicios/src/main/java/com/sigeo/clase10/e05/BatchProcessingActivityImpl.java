package com.sigeo.clase10.e05;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;

public class BatchProcessingActivityImpl implements BatchProcessingActivity {

    private boolean simulateCrash = true;

    @Override
    public int processBatch(int totalRecords) {
        ActivityExecutionContext context = Activity.getExecutionContext();
        
        // TODO(C10-E05): Recuperar el último offset desde el heartbeat (si existe).
        // Si no hay heartbeat previo, empezar desde 0.
        int startOffset = 0; // Cambiar esto
        
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
            
            // TODO(C10-E05): Emitir un heartbeat cada 100 registros procesados.
            // Pasar 'processed' como detalle del heartbeat.
            
            // Simulamos un crash a la mitad del procesamiento en el primer intento
            if (simulateCrash && processed == 500) {
                simulateCrash = false;
                throw new RuntimeException("Simulated crash at 500");
            }
        }
        
        return processed;
    }
}
