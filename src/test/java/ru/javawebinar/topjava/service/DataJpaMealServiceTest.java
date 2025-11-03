package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, value = "datajpa")
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void getMealWithUser() {
        Meal actual = service.getWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(actual, meal1);
        assertNotNull(actual.getUser());
    }

    @Test
    public void getMealsWithUser() {
        List<Meal> allUserMeal = service.getAllWithUser(USER_ID);
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), allUserMeal);
    }
}
