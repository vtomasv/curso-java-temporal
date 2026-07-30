package com.sigeo.clase03;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class OperationTrackerTest {

    @Test
    void shouldClearMdcAfterOperation() {
        OperationTracker tracker = new OperationTracker();
        
        assertThatCode(() -> tracker.processOperation("corr-123", "user-456", "secret-token"))
            .doesNotThrowAnyException();
            
        // MDC debe estar limpio después de la operación
        assertThat(MDC.get("correlationId")).isNull();
    }
}
