package com.sigeo.clase12;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;

class SagaActivitiesTest {

    @Test
    void testDobleCancelacion() {
        SagaActivitiesImpl activities = new SagaActivitiesImpl();
        
        assertThatCode(() -> {
            activities.cancelResource("res-1");
            activities.cancelResource("res-1");
        }).doesNotThrowAnyException();
    }
}
