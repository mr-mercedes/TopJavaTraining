package ru.javawebinar.topjava.web.meal;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.meal.MealRestController.BASE_PATH;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(BASE_PATH + "/{id}", MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(BASE_PATH))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> new AssertionMatcher<List<Meal>>() {
                    @Override
                    public void assertion(List<Meal> actual) throws AssertionError {
                        MEAL_MATCHER.assertMatch(actual, meals);
                    }
                });
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{id}", MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> mealService.get(USER_ID, MEAL1_ID));
    }

    @Test
    void create() throws Exception {
        Meal newMeal = getNew();
        ResultActions action = perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(BASE_PATH + "/{id}")
                .buildAndExpand(newId).toUri();
        action.andExpect(header().string("Location", uriOfNewResource.toString()));
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(BASE_PATH + "/{id}", MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(updated.id(), USER_ID), updated);
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(BASE_PATH + "/between")
                .queryParam("startDate", "2020-01-30")
                .queryParam("startTime", "10:00:10")
                .queryParam("endDate", "2020-01-30")
                .queryParam("endTime", "10:10:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> new AssertionMatcher<List<Meal>>() {
                    @Override
                    public void assertion(List<Meal> actual) throws AssertionError {
                        MEAL_MATCHER.assertMatch(actual, List.of(meal1));
                    }
                });
    }
}