package com.sigeo.clase09;

import io.temporal.workflow.Workflow;
import java.time.Instant;
import java.util.UUID;

// TODO(C09-E07): Esta clase contiene múltiples violaciones de determinismo.
// Identifica y corrige los errores.
public class NoDeterministaWorkflowImpl {

    public String ejecutarProceso() {
        // ERROR 1: Uso de UUID.randomUUID()
        String id = UUID.randomUUID().toString();
        
        // ERROR 2: Uso de Instant.now()
        Instant inicio = Instant.now();
        
        // ERROR 3: Uso de Thread.sleep()
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // ERROR 4: Uso de Math.random()
        double random = Math.random();
        
        // ERROR 5: Imprimir a consola directamente desde el workflow
        System.out.println("Proceso ejecutado: " + id);
        
        return "Completado";
    }
}
