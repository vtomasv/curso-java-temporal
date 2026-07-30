package com.sigeo.clase15.publisher;

import com.sigeo.clase15.config.RabbitMQConfig;
import com.sigeo.clase15.model.NotificationRequested;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(NotificationRequested notification) {
        // TODO(C15-E02): Implementar la publicación del mensaje
        // Usar rabbitTemplate.convertAndSend
        // Configurar el correlationId en las propiedades del mensaje
        throw new UnsupportedOperationException("TODO C15-E02: Implementar publicación de mensaje");
    }
}
