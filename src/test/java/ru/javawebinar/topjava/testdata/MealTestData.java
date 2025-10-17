package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int MEAL_ID = 100003;
    public static final int NOT_FOUND_USER = 10;
    public static final int NOT_FOUND_MEAL = 404;
    public static final LocalDate START_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate END_DATE = LocalDate.of(2020, Month.JANUARY, 31);

    private static final LocalDateTime UPDATED_DATETIME =
            LocalDateTime.of(1995, Month.MAY, 23, 10, 0);
    private static final String UPDATED_DESCRIPTION = "Торт";

    public static Meal USER_MEAL_STANDARD =
            new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    private static final Meal USER_MEAL_01_30_2020_LAUNCH =
            new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    private static final Meal USER_MEAL_01_30_2020_DINNER =
            new Meal(100005, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    private static final Meal USER_MEAL_01_31_2020_BORDER =
            new Meal(100006, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    private static final Meal USER_MEAL_01_31_2020_BREAKFAST =
            new Meal(100007, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    private static final Meal USER_MEAL_01_31_2020_LAUNCH =
            new Meal(100008, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    private static final Meal USER_MEAL_01_31_2020_DINNER =
            new Meal(100009, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final List<Meal> USER_MEALS = Arrays.asList(
            USER_MEAL_01_31_2020_DINNER, USER_MEAL_01_31_2020_LAUNCH, USER_MEAL_01_31_2020_BREAKFAST,
            USER_MEAL_01_31_2020_BORDER, USER_MEAL_01_30_2020_DINNER, USER_MEAL_01_30_2020_LAUNCH, USER_MEAL_STANDARD
    );

    public static final List<Meal> FILTERED_MEALS = Arrays.asList(
            USER_MEAL_01_31_2020_DINNER, USER_MEAL_01_31_2020_LAUNCH, USER_MEAL_01_31_2020_BREAKFAST, USER_MEAL_01_31_2020_BORDER
    );

    private static final Meal ADMIN_MEAL_06_01_2025_LAUNCH =
            new Meal(100010, LocalDateTime.of(2025, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    private static final Meal ADMIN_MEAL_06_01_2025_DINNER =
            new Meal(100011, LocalDateTime.of(2025, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static final List<Meal> ADMIN_MEALS = Arrays.asList(
            ADMIN_MEAL_06_01_2025_DINNER, ADMIN_MEAL_06_01_2025_LAUNCH
    );

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2025, Month.OCTOBER, 17, 10, 0), "Новая еда", 777);
    }

    public static Meal getDuplicate() {
        return new Meal(LocalDateTime.of(2025, Month.OCTOBER, 17, 10, 0), "Дублирующая еда", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL_STANDARD);
        updated.setDateTime(UPDATED_DATETIME);
        updated.setDescription(UPDATED_DESCRIPTION);
        updated.setCalories(30);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertNotMatch(Iterable<Meal> one, Iterable<Meal> other) {
        assertThat(one).usingRecursiveComparison().isNotIn(other);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
