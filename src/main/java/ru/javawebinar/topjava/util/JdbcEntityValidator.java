package ru.javawebinar.topjava.util;


import javax.validation.*;
import java.util.Set;

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
            throw new ConstraintViolationException(violations);
        }
    }
}
