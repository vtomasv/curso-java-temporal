package com.sigeo.clase10.e04;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReservationActivityImpl implements ReservationActivity {
    
    // Simula una base de datos de reservas
    private final Map<String, String> reservations = new ConcurrentHashMap<>();
    private final AtomicInteger callCount = new AtomicInteger();

    @Override
    public String makeReservation(String itemId, String idempotencyKey) {
        callCount.incrementAndGet();
        return reservations.computeIfAbsent(idempotencyKey, ignored -> "RES-" + itemId);
    }

    public int getCallCount() {
        return callCount.get();
    }

    public int getReservationCount() {
        return reservations.size();
    }
}
