package ru.javawebinar.topjava.repository.jdbc;


import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.Set;

@Component
public class JdbcEntityValidator {

    private final Validator validator;

    public JdbcEntityValidator() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.getValidator();
        }

    }

    public <T> void validateOrThrow(T target) {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed", new ConstraintViolationException(violations));
        }
    }
}
