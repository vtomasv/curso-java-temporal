package com.sigeo.clase03;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class PriorityCalculatorTest {

    @Test
    void shouldNotFailOnEndOfMonthWithMaxPriority() {
        PriorityCalculator calculator = new PriorityCalculator();
        LocalDate endOfMonth = LocalDate.of(2026, 8, 31);
        
        int priority = calculator.calculatePriority(10, endOfMonth);
        
        assertThat(priority).isEqualTo(100);
    }
}
