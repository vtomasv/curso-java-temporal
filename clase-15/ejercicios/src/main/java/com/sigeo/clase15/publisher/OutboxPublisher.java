package com.sigeo.clase15.publisher;

import com.sigeo.clase15.config.RabbitMQConfig;
import com.sigeo.clase15.model.OutboxEvent;
import com.sigeo.clase15.repository.OutboxRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxPublisher(OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // TODO(C15-E06): Implementar publicación desde Outbox
    // 1. Usar @Scheduled(fixedDelay = 5000)
    // 2. Usar @Transactional
    // 3. Buscar eventos PENDING
    // 4. Publicar cada evento y actualizar su estado a PUBLISHED
    public void publishPendingEvents() {
        throw new UnsupportedOperationException("TODO C15-E06: Implementar publicación desde Outbox");
    }
}
