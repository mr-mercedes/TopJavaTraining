package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class MealTestData {

    public static final int MEAL_ID = 100003;
    public static final int NOT_FOUND_ID = 404;
    public static final LocalDate START_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate END_DATE = LocalDate.of(2020, Month.JANUARY, 31);

    private static final LocalDateTime updatedDateTime =
            LocalDateTime.of(1995, Month.MAY, 23, 10, 0);
    private static final String updatedDescription = "Торт";

    public static Meal userMealStandard =
            new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    private static final Meal userMeal01 =
            new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    private static final Meal userMeal02 =
            new Meal(100005, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    private static final Meal userMeal03 =
            new Meal(100006, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    private static final Meal userMeal04 =
            new Meal(100007, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    private static final Meal userMeal05 =
            new Meal(100008, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    private static final Meal userMeal06 =
            new Meal(100009, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final List<Meal> USER_MEALS = Collections.unmodifiableList(Arrays.asList(
            userMeal06, userMeal05, userMeal04,
            userMeal03, userMeal02, userMeal01, userMealStandard
    ));

    public static final List<Meal> FILTERED_MEALS = Collections.unmodifiableList(Arrays.asList(
            userMeal06, userMeal05, userMeal04, userMeal03
    ));

    private static final Meal adminMealLaunch =
            new Meal(100010, LocalDateTime.of(2025, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    private static final Meal adminMealDinner =
            new Meal(100011, LocalDateTime.of(2025, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static final List<Meal> ADMIN_MEALS = Collections.unmodifiableList(Arrays.asList(
            adminMealDinner, adminMealLaunch
    ));

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2025, Month.OCTOBER, 17, 10, 0), "Новая еда", 777);
    }

    public static Meal getDuplicate() {
        return new Meal(LocalDateTime.of(2025, Month.OCTOBER, 17, 10, 0), "Дублирующая еда", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMealStandard);
        updated.setDateTime(updatedDateTime);
        updated.setDescription(updatedDescription);
        updated.setCalories(30);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertNotMatch(Collection<Meal> one, Collection<Meal> other) {
        assertTrue(Collections.disjoint(one, other));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
