package com.sigeo.clase03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class OperationTracker {
    private static final Logger logger = LoggerFactory.getLogger(OperationTracker.class);

    public void processOperation(String correlationId, String userId, String secretToken) {
        // TODO(C03-E03): Configurar MDC con el correlationId.
        // Loguear a nivel INFO el inicio de la operación con el userId (usar parámetros {}, no concatenación).
        // NUNCA loguear el secretToken.
        // Loguear a nivel DEBUG un mensaje simulando un procesamiento costoso.
        // Asegurarse de limpiar el MDC al finalizar (usar try-finally).
        throw new UnsupportedOperationException("TODO C03-E03");
    }
}
