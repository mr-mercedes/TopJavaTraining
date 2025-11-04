package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;

import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.GUEST_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(value = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    private UserService service;

    @Autowired
    private MealService mealService;

    @Test
    public void getByIdWithMeals() {
        List<Meal> meals = service.getByIdWithMeals(USER_ID);
        MEAL_MATCHER.assertMatch(meals, mealService.getAll(USER_ID));
    }

    @Test
    public void getByIdWithoutMeals() {
        List<Meal> meals = service.getByIdWithMeals(GUEST_ID);
        MEAL_MATCHER.assertMatch(meals, Collections.emptyList());
    }
}
