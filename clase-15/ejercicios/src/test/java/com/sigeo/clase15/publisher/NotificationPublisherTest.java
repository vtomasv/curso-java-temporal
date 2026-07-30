package com.sigeo.clase15.publisher;

import com.sigeo.clase15.config.RabbitMQConfig;
import com.sigeo.clase15.model.NotificationRequested;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
class NotificationPublisherTest {

    @Autowired
    private NotificationPublisher publisher;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldPublishNotificationWithCorrelationId() {
        NotificationRequested notification = new NotificationRequested("corr-123", "user@test.com", "Hello");

        publisher.publish(notification);

        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.EXCHANGE_NAME),
            eq(RabbitMQConfig.ROUTING_KEY),
            eq(notification),
            any(org.springframework.amqp.core.MessagePostProcessor.class)
        );
    }
}
