package com.sigeo.clase15.publisher;

import com.sigeo.clase15.model.OutboxEvent;
import com.sigeo.clase15.repository.OutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class OutboxPublisherTest {

    @Autowired
    private OutboxPublisher publisher;

    @Autowired
    private OutboxRepository repository;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldPublishPendingEventsAndMarkAsPublished() {
        OutboxEvent event1 = new OutboxEvent("evt-1", "TypeA", "{}");
        OutboxEvent event2 = new OutboxEvent("evt-2", "TypeA", "{}");
        repository.save(event1);
        repository.save(event2);

        publisher.publishPendingEvents();

        verify(rabbitTemplate, times(2)).convertAndSend(anyString(), anyString(), any(Object.class));
        
        assertThat(repository.findByStatus("PENDING")).isEmpty();
        assertThat(repository.findByStatus("PUBLISHED")).hasSize(2);
    }
}
