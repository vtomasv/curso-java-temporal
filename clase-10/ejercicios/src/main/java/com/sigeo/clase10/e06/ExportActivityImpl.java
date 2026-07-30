package com.sigeo.clase10.e06;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;

public class ExportActivityImpl implements ExportActivity {

    private boolean cleanupCalled = false;

    @Override
    public void exportData() {
        ActivityExecutionContext context = Activity.getExecutionContext();
        
        try {
            for (int i = 0; i < 100; i++) {
                // TODO(C10-E06): Emitir heartbeat para permitir la cancelación cooperativa.
                // Si no se emite heartbeat, la actividad no sabrá que fue cancelada.
                
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
            // TODO(C10-E06): Ejecutar lógica de limpieza (cleanup) si la actividad fue cancelada.
            // Puedes verificar si fue cancelada capturando la excepción o verificando el contexto.
            // Para este ejercicio, simplemente marca cleanupCalled = true en el bloque finally.
            cleanupCalled = true;
        }
    }

    public boolean isCleanupCalled() {
        return cleanupCalled;
    }
}
