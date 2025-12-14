package ru.javawebinar.topjava.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "{common.user.email.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
