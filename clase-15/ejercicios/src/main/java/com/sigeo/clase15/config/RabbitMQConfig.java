package com.sigeo.clase15.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "sigeo.exchange";
    public static final String QUEUE_NAME = "sigeo.notification.queue";
    public static final String ROUTING_KEY = "sigeo.notification.routing.key";
    
    public static final String DLX_NAME = "sigeo.dlx";
    public static final String DLQ_NAME = "sigeo.dlq";

    // TODO(C15-E05): Configurar Dead Letter Exchange y Queue
    // 1. Crear bean para DLX (DirectExchange)
    // 2. Crear bean para DLQ (Queue)
    // 3. Crear bean para Binding entre DLQ y DLX

    // TODO(C15-E02): Configurar Exchange, Queue y Binding principales
    // 1. Crear bean para Exchange principal (DirectExchange)
    // 2. Crear bean para Queue principal (QueueBuilder.durable... con argumentos para DLX)
    // 3. Crear bean para Binding principal

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
