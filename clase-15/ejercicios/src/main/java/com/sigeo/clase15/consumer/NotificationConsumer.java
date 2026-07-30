package com.sigeo.clase15.consumer;

import com.sigeo.clase15.config.RabbitMQConfig;
import com.sigeo.clase15.model.NotificationRequested;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final List<NotificationRequested> receivedNotifications = new ArrayList<>();

    // TODO(C15-E02): Configurar @RabbitListener para la cola correcta
    // TODO(C15-E03): Agregar @Valid para validar el payload
    public void receiveMessage(NotificationRequested notification, @Header(AmqpHeaders.CORRELATION_ID) String correlationId) {
        log.info("Received notification: {} with correlationId: {}", notification, correlationId);
        receivedNotifications.add(notification);
    }

    public List<NotificationRequested> getReceivedNotifications() {
        return receivedNotifications;
    }
    
    public void clear() {
        receivedNotifications.clear();
    }
}
