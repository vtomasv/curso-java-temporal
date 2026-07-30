package com.sigeo.clase19;

import org.springframework.transaction.annotation.Transactional;

public class ErroresComunes {

    public void procesarDatos() {
        try {
            // Simular procesamiento
            int a = 1 / 0;
        } catch (Exception e) {
            // TODO(C19-E06): Corregir catch vacío
        }
    }
    
    // TODO(C19-E06): Corregir @Transactional mal ubicado
    @Transactional
    private void metodoPrivado() {
        // Operación de base de datos
    }
    
    public void ejecutarWorkflow() {
        // TODO(C19-E06): Corregir HTTP en Workflow
        // RestTemplate restTemplate = new RestTemplate();
        // restTemplate.getForObject("http://api.example.com", String.class);
    }
}
