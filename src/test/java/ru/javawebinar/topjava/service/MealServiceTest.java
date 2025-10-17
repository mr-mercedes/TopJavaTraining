package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.testdata.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer createdId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(createdId);
        assertMatch(created, newMeal);
        assertMatch(service.get(createdId, USER_ID), newMeal);
    }

    @Test
    public void createDuplicateMeal() {
        service.create(getNew(), USER_ID);
        assertThrows(DataAccessException.class, () ->
                service.create(getDuplicate(), USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(updated, service.get(updated.getId(), USER_ID));
    }

    @Test
    public void updateNotFoundMeal() {
        Meal updated = getUpdated();
        updated.setId(NOT_FOUND_ID);
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }

    @Test
    public void updateNotFoundUser() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, NOT_FOUND_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, USER_MEAL_STANDARD);
    }

    @Test
    public void getNotFoundMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void getNotFoundUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, NOT_FOUND_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFoundMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void deleteNotFoundUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, NOT_FOUND_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, USER_MEALS);
    }

    @Test
    public void getAllDifferentUsersMeals() {
        List<Meal> userMeals = service.getAll(USER_ID);
        List<Meal> adminMeals = service.getAll(ADMIN_ID);
        assertNotMatch(userMeals, adminMeals);
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(START_DATE, END_DATE, USER_ID);
        assertMatch(betweenInclusive, USER_MEALS);
    }

    @Test
    public void getBetweenInclusiveWithoutStartDate() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(null, END_DATE, USER_ID);
        assertMatch(betweenInclusive, USER_MEALS);
    }

    @Test
    public void getBetweenInclusiveWithoutEndDate() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(END_DATE, null, USER_ID);
        assertMatch(betweenInclusive, FILTERED_MEALS);
    }

    @Test
    public void getBetweenInclusiveWithoutFilters() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(null, null, ADMIN_ID);
        assertMatch(betweenInclusive, ADMIN_MEALS);
    }

}