package com.sigeo.clase17;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "sigeo.notifications";
    public static final String DLQ_NAME = "sigeo.notifications.dlq";

    // TODO(C17-E06): Configurar DLQ y redelivery
    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}
