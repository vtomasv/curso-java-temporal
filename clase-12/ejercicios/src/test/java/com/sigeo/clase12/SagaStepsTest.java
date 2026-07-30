package com.sigeo.clase12;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class SagaStepsTest {

    @Test
    void testSagaSteps() {
        SagaSteps sagaSteps = new SagaSteps();
        List<SagaSteps.StepDefinition> steps = sagaSteps.getSagaSteps();

        assertThat(steps).hasSize(4);
        assertThat(steps.get(0).name()).isEqualTo("Reserva");
        assertThat(steps.get(0).compensation()).isNotBlank();
    }
}
