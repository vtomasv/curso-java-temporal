package com.sigeo.clase15.consumer;

import com.sigeo.clase15.model.NotificationRequestedV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VersionTolerantConsumer {

    private static final Logger log = LoggerFactory.getLogger(VersionTolerantConsumer.class);
    private final List<NotificationRequestedV2> receivedNotifications = new ArrayList<>();

    // TODO(C15-E08): Implementar consumidor tolerante a versiones
    // 1. Configurar @RabbitListener
    // 2. Recibir NotificationRequestedV2
    // 3. Usar getPriorityOrDefault()
    public void receiveMessage(NotificationRequestedV2 notification) {
        throw new UnsupportedOperationException("TODO C15-E08: Implementar consumidor tolerante a versiones");
    }

    public List<NotificationRequestedV2> getReceivedNotifications() {
        return receivedNotifications;
    }
    
    public void clear() {
        receivedNotifications.clear();
    }
}
