package com.sigeo.clase12;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;

class OutboxTest {

    @Test
    void testSaveEvent() {
        OutboxService service = new OutboxService();
        assertThatCode(() -> service.saveEvent("evt-1", "{}"))
                .doesNotThrowAnyException();
    }
}
