package com.sigeo.clase15.consumer;

import com.sigeo.clase15.model.NotificationRequested;
import com.sigeo.clase15.repository.InboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IdempotentConsumerTest {

    @Autowired
    private IdempotentConsumer consumer;

    @Autowired
    private InboxRepository inboxRepository;

    @BeforeEach
    void setUp() {
        consumer.reset();
    }

    @Test
    void shouldProcessMessageOnlyOnce() {
        NotificationRequested notification = new NotificationRequested("corr-1", "user@test.com", "msg");
        String messageId = "msg-123";

        consumer.receiveMessage(notification, messageId);
        consumer.receiveMessage(notification, messageId); // Duplicate

        assertThat(consumer.getProcessCount()).isEqualTo(1);
        assertThat(inboxRepository.existsById(messageId)).isTrue();
    }
}
