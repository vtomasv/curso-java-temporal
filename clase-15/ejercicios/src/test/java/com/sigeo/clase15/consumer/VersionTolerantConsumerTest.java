package com.sigeo.clase15.consumer;

import com.sigeo.clase15.model.NotificationRequestedV2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VersionTolerantConsumerTest {

    @Autowired
    private VersionTolerantConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer.clear();
    }

    @Test
    void shouldHandleMissingPriorityWithDefault() {
        NotificationRequestedV2 notification = new NotificationRequestedV2("corr-1", "user@test.com", "msg", null);

        consumer.receiveMessage(notification);

        assertThat(consumer.getReceivedNotifications()).hasSize(1);
        assertThat(consumer.getReceivedNotifications().get(0).getPriorityOrDefault()).isEqualTo("NORMAL");
    }
    
    @Test
    void shouldHandleProvidedPriority() {
        NotificationRequestedV2 notification = new NotificationRequestedV2("corr-1", "user@test.com", "msg", "HIGH");

        consumer.receiveMessage(notification);

        assertThat(consumer.getReceivedNotifications()).hasSize(1);
        assertThat(consumer.getReceivedNotifications().get(0).getPriorityOrDefault()).isEqualTo("HIGH");
    }
}
