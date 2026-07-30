package com.sigeo.clase03;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class StateServiceTest {

    @Test
    void shouldTranslateExceptionAndPreserveCause() {
        StateService service = new StateService();
        
        Throwable thrown = catchThrowable(() -> service.transitionState("FINAL", "ARCHIVED"));
        
        assertThat(thrown)
            .isInstanceOf(InvalidStateTransitionException.class)
            .hasMessage("No se puede transicionar de FINAL a ARCHIVED");
            
        assertThat(thrown.getCause())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Estado final inmutable");
    }
}
