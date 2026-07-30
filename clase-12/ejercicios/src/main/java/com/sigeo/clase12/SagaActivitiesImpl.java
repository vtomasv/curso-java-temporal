package com.sigeo.clase12;

import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SagaActivitiesImpl implements SagaActivities {

    private final Set<String> cancelledResources = ConcurrentHashMap.newKeySet();

    @Override
    public void reserveResource(String reservationId) {
        System.out.println("Reserving resource for " + reservationId);
    }

    @Override
    public void cancelResource(String reservationId) {
        // TODO(C12-E05): Compensar dos veces sin error ni efecto duplicado.
        // Implementar idempotencia usando cancelledResources.
        throw new UnsupportedOperationException("TODO C12-E05");
    }

    @Override
    public void allocateBudget(String reservationId, boolean fail) {
        if (fail) {
            throw new RuntimeException("Simulated budget allocation failure");
        }
        System.out.println("Allocating budget for " + reservationId);
    }

    @Override
    public void releaseBudget(String reservationId) {
        System.out.println("Releasing budget for " + reservationId);
    }

    @Override
    public void scheduleAgenda(String reservationId, boolean fail) {
        if (fail) {
            throw new RuntimeException("Simulated agenda scheduling failure");
        }
        System.out.println("Scheduling agenda for " + reservationId);
    }

    @Override
    public void cancelAgenda(String reservationId) {
        System.out.println("Canceling agenda for " + reservationId);
    }

    @Override
    public void sendNotification(String reservationId, boolean fail) {
        if (fail) {
            throw new RuntimeException("Simulated notification failure");
        }
        System.out.println("Sending notification for " + reservationId);
    }
}
