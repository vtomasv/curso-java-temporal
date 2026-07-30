package com.sigeo.clase15.consumer;

import com.sigeo.clase15.model.NotificationRequested;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TemporalBridgeConsumer {

    private static final Logger log = LoggerFactory.getLogger(TemporalBridgeConsumer.class);
    private final WorkflowClient workflowClient;

    public TemporalBridgeConsumer(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    // TODO(C15-E07): Implementar puente entre RabbitMQ y Temporal
    // 1. Configurar @RabbitListener
    // 2. Crear WorkflowOptions usando correlationId como WorkflowId para idempotencia
    // 3. Iniciar el workflow (manejar WorkflowExecutionAlreadyStartedException si es necesario)
    public void handleMessage(NotificationRequested notification) {
        throw new UnsupportedOperationException("TODO C15-E07: Implementar puente Temporal");
    }
}
