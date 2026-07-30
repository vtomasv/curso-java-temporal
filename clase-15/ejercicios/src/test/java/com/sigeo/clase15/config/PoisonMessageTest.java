package com.sigeo.clase15.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PoisonMessageTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldHaveDlqConfigured() {
        Queue dlq = context.getBean("deadLetterQueue", Queue.class);
        assertThat(dlq.getName()).isEqualTo(RabbitMQConfig.DLQ_NAME);

        DirectExchange dlx = context.getBean("deadLetterExchange", DirectExchange.class);
        assertThat(dlx.getName()).isEqualTo(RabbitMQConfig.DLX_NAME);

        Queue mainQueue = context.getBean("queue", Queue.class);
        assertThat(mainQueue.getArguments()).containsEntry("x-dead-letter-exchange", RabbitMQConfig.DLX_NAME);
    }
}
