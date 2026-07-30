package com.sigeo.clase17;

import org.springframework.stereotype.Component;

@Component
public class SigeoActivitiesImpl implements SigeoActivities {

    @Override
    public void approveRequest(String requestId) {
        // TODO(C17-E02): Implementar aprobación en DB
        throw new UnsupportedOperationException("TODO C17-E02: Implementar aprobación");
    }

    @Override
    public void notifyUser(String requestId) {
        // TODO(C17-E02): Implementar notificación vía RabbitMQ
        throw new UnsupportedOperationException("TODO C17-E02: Implementar notificación");
    }

    @Override
    public void compensateRequest(String requestId) {
        // TODO(C17-E03): Implementar compensación
        throw new UnsupportedOperationException("TODO C17-E03: Implementar compensación");
    }
}
