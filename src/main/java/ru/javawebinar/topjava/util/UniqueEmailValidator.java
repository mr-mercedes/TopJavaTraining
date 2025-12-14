package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.hasText;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, UserTo> {

    private final UserRepository repository;

    public UniqueEmailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(UserTo to, ConstraintValidatorContext ctx) {
        if (to == null) return true;

        String email = to.getEmail();
        if (!hasText(email)) return true;

        User user = repository.getByEmail(email);
        if (user == null) return true;

        Integer id = to.getId();
        boolean ok = (id != null && user.id() == id);

        if (!ok) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email")
                    .addConstraintViolation();
        }
        return ok;
    }
}
