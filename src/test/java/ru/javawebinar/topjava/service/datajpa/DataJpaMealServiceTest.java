package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(value = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void getWithUser() {
        Meal actual = service.getByIdWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(actual, meal1);
        assertNotNull(actual.getUser());
    }

    @Test
    public void getNotOwnWithUser() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithUser(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getNotFoundWithUser() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithUser(NOT_FOUND, USER_ID));
    }
}
