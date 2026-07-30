package com.sigeo.clase15.consumer;

import com.sigeo.clase15.model.NotificationRequested;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TemporalBridgeConsumerTest {

    @Autowired
    private TemporalBridgeConsumer consumer;

    @MockitoBean
    private WorkflowClient workflowClient;

    @Test
    void shouldStartWorkflowWithCorrelationIdAsWorkflowId() {
        NotificationRequested notification = new NotificationRequested("corr-123", "user@test.com", "msg");

        consumer.handleMessage(notification);

        verify(workflowClient).newWorkflowStub(any(Class.class), argThat((WorkflowOptions options) -> 
            "corr-123".equals(options.getWorkflowId())
        ));
    }
}
