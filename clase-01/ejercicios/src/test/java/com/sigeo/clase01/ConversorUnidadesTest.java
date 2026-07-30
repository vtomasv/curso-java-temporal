package com.sigeo.clase01;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class ConversorUnidadesTest {

    @Test
    void debeConvertirCelsiusAFahrenheit() {
        assertThat(ConversorUnidades.celsiusAFahrenheit(0)).isCloseTo(32.0, within(0.01));
        assertThat(ConversorUnidades.celsiusAFahrenheit(100)).isCloseTo(212.0, within(0.01));
        assertThat(ConversorUnidades.celsiusAFahrenheit(-40)).isCloseTo(-40.0, within(0.01));
    }

    @Test
    void debeConvertirKilometrosAMillas() {
        assertThat(ConversorUnidades.kilometrosAMillas(1)).isCloseTo(0.621371, within(0.0001));
        assertThat(ConversorUnidades.kilometrosAMillas(5)).isCloseTo(3.106855, within(0.0001));
        assertThat(ConversorUnidades.kilometrosAMillas(0)).isCloseTo(0.0, within(0.0001));
    }
}
