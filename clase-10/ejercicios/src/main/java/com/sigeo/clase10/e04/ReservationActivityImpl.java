package com.sigeo.clase10.e04;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReservationActivityImpl implements ReservationActivity {
    
    // Simula una base de datos de reservas
    private final Map<String, String> reservations = new ConcurrentHashMap<>();
    private int callCount = 0;

    @Override
    public String makeReservation(String itemId, String idempotencyKey) {
        callCount++;
        // TODO(C10-E04): Implementar lógica de idempotencia.
        // Si la idempotencyKey ya existe en el mapa 'reservations', retorna el valor guardado.
        // Si no, crea una nueva reserva (ej. "RES-" + itemId), guárdala en el mapa y retórnala.
        throw new UnsupportedOperationException("TODO C10-E04");
    }

    public int getCallCount() {
        return callCount;
    }
}
