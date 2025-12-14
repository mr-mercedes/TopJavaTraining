package ru.javawebinar.topjava;

import org.junit.jupiter.api.Named;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "meals", "password");
    public static MatcherFactory.Matcher<User> USER_WITH_MEALS_MATCHER =
            MatcherFactory.usingAssertions(User.class,
//     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("registered", "meals.user", "password").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", 2005, Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", 1900, Role.ADMIN, Role.USER);
    public static final User guest = new User(GUEST_ID, "Guest", "guest@gmail.com", "guest", 2000);

    static {
        user.setMeals(meals);
        admin.setMeals(List.of(adminMeal2, adminMeal1));
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static Stream<Named<UserTo>> invalidUpdatedUserTo() {
        UserTo invalidNameNull = new UserTo(user.id(), null, user.getEmail(), user.getPassword(), user.getCaloriesPerDay());
        UserTo invalidNameBlank = new UserTo(user.id(), "", user.getEmail(), user.getPassword(), user.getCaloriesPerDay());
        UserTo invalidNameShort = new UserTo(user.id(), "1", user.getEmail(), user.getPassword(), user.getCaloriesPerDay());
        UserTo invalidNameLong = new UserTo(user.id(), "a".repeat(101), user.getEmail(), user.getPassword(), user.getCaloriesPerDay());

        UserTo invalidEmail = new UserTo(user.id(), user.getName(), "no-email", user.getPassword(), user.getCaloriesPerDay());
        UserTo invalidEmailNull = new UserTo(user.id(), user.getName(), null, user.getPassword(), user.getCaloriesPerDay());
        UserTo invalidEmailBlank = new UserTo(user.id(), user.getName(), "", user.getPassword(), user.getCaloriesPerDay());
        String longEmail = String.format("valid-email%s@mail.ru", "a".repeat(101 - "valid-email@mail.ru".length()));
        UserTo invalidEmailLong = new UserTo(user.id(), user.getName(), longEmail, user.getPassword(), user.getCaloriesPerDay());

        UserTo invalidPassNull = new UserTo(user.id(), user.getName(), user.getEmail(), null, user.getCaloriesPerDay());
        UserTo invalidPassBlank = new UserTo(user.id(), user.getName(), user.getEmail(), "", user.getCaloriesPerDay());
        UserTo invalidPassShort = new UserTo(user.id(), user.getName(), user.getEmail(), "1111", user.getCaloriesPerDay());
        UserTo invalidPassLong = new UserTo(user.id(), user.getName(), user.getEmail(), "1".repeat(33), user.getCaloriesPerDay());

        UserTo invalidCaloriesLess = new UserTo(user.id(), user.getName(), user.getEmail(), user.getPassword(), 9);
        UserTo invalidCaloriesMore = new UserTo(user.id(), user.getName(), user.getEmail(), user.getPassword(), 10001);

        return Stream.of(
                Named.of("invalidNameNull", invalidNameNull),
                Named.of("invalidNameBlank", invalidNameBlank),
                Named.of("invalidNameShort", invalidNameShort),
                Named.of("invalidNameLong", invalidNameLong),
                Named.of("invalidEmail", invalidEmail),
                Named.of("invalidEmailNull", invalidEmailNull),
                Named.of("invalidEmailBlank", invalidEmailBlank),
                Named.of("invalidEmailLong", invalidEmailLong),
                Named.of("invalidPassNull", invalidPassNull),
                Named.of("invalidPassBlank", invalidPassBlank),
                Named.of("invalidPassShort", invalidPassShort),
                Named.of("invalidPassLong", invalidPassLong),
                Named.of("invalidCaloriesLess", invalidCaloriesLess),
                Named.of("invalidCaloriesMore", invalidCaloriesMore)

        );
    }

    public static Stream<Named<UserTo>> invalidRegisteredUserTo() {
        return invalidUpdatedUserTo().peek(u -> u.getPayload().setId(null));
    }
}
