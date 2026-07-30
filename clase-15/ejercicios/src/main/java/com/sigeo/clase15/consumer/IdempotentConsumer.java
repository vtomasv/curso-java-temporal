package com.sigeo.clase15.consumer;

import com.sigeo.clase15.model.InboxMessage;
import com.sigeo.clase15.model.NotificationRequested;
import com.sigeo.clase15.repository.InboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IdempotentConsumer {

    private static final Logger log = LoggerFactory.getLogger(IdempotentConsumer.class);
    private final InboxRepository inboxRepository;
    private final AtomicInteger processCount = new AtomicInteger(0);

    public IdempotentConsumer(InboxRepository inboxRepository) {
        this.inboxRepository = inboxRepository;
    }

    // TODO(C15-E04): Implementar consumidor idempotente
    // 1. Configurar @RabbitListener para una cola específica (ej. sigeo.idempotent.queue)
    // 2. Usar @Transactional
    // 3. Verificar si el messageId ya existe en inboxRepository
    // 4. Si no existe, incrementar processCount y guardar en inboxRepository
    public void receiveMessage(NotificationRequested notification, @Header(AmqpHeaders.MESSAGE_ID) String messageId) {
        throw new UnsupportedOperationException("TODO C15-E04: Implementar consumidor idempotente");
    }

    public int getProcessCount() {
        return processCount.get();
    }
    
    public void reset() {
        processCount.set(0);
        inboxRepository.deleteAll();
    }
}
