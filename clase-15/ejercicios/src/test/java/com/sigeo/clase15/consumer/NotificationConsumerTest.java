package com.sigeo.clase15.consumer;

import com.sigeo.clase15.model.NotificationRequested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotificationConsumerTest {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    void shouldFailValidationWhenFieldsAreBlank() {
        NotificationRequested invalidNotification = new NotificationRequested("", "", "");

        Set<ConstraintViolation<NotificationRequested>> violations = validator.validate(invalidNotification);

        assertThat(violations).hasSize(3);
    }
    
    @Test
    void shouldPassValidationWhenFieldsAreValid() {
        NotificationRequested validNotification = new NotificationRequested("corr-1", "user@test.com", "msg");

        Set<ConstraintViolation<NotificationRequested>> violations = validator.validate(validNotification);

        assertThat(violations).isEmpty();
    }
}
