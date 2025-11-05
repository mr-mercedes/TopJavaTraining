package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;

import java.util.Collections;

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
        User userWithMeals = service.getByIdWithMeals(USER_ID);
        MEAL_MATCHER.assertMatch(userWithMeals.getMeals(), mealService.getAll(USER_ID));
    }

    @Test
    public void getByIdWithoutMeals() {
        User guessWithMeals = service.getByIdWithMeals(GUEST_ID);
        MEAL_MATCHER.assertMatch(guessWithMeals.getMeals(), Collections.emptyList());
    }
}
