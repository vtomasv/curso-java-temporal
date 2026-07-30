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
        // TODO(C03-E06): Implementar lógica de reintento.
        // Intentar llamar a repository.fetchData() hasta 2 veces.
        // Si falla, loguear el intento (ej. "Intento 1 falló: ...").
        // Si falla en todos los intentos permitidos, relanzar la última excepción.
        throw new UnsupportedOperationException("TODO C03-E06");
    }
}
