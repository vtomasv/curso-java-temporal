package com.sigeo.clase12;

import org.springframework.stereotype.Service;

@Service
public class OutboxService {

    public void saveEvent(String eventId, String payload) {
        // TODO(C12-E08): Diseñar tabla outbox y publicador idempotente al finalizar saga.
        // Guardar el evento en la base de datos de forma transaccional.
        throw new UnsupportedOperationException("TODO C12-E08");
    }

    public void publishPendingEvents() {
        // TODO(C12-E08): Leer eventos pendientes y publicarlos.
        throw new UnsupportedOperationException("TODO C12-E08");
    }
}
