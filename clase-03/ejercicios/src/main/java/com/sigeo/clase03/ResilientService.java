package com.sigeo.clase03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResilientService {
    private static final Logger logger = LoggerFactory.getLogger(ResilientService.class);
    private final UnstableRepository repository;

    public ResilientService(UnstableRepository repository) {
        this.repository = repository;
    }

    public String getReliableData() {
        
        int maxRetries = 2;
        int attempt = 0;
        
        while (true) {
            try {
                attempt++;
                return repository.fetchData();
            } catch (RuntimeException e) {
                if (attempt > maxRetries) {
                    logger.error("Todos los intentos fallaron. Último error: {}", e.getMessage());
                    throw e;
                }
                logger.warn("Intento {} falló: {}. Reintentando...", attempt, e.getMessage());
            }
        }
    }
}
