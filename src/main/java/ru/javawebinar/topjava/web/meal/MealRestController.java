package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("MealRestController getAll");
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        return service.getAll(userId, caloriesPerDay);
    }

    public MealTo getById(int id) {
        log.info("MealRestController getById: {}", id);
        int userId = SecurityUtil.authUserId();
        return service.get(userId, id);
    }

    public MealTo create(MealTo mealTo) {
        log.info("MealRestController create: {}", mealTo);
        int userId = SecurityUtil.authUserId();
        Meal meal = MealsUtil.fromTo(mealTo);
        return service.createWithLocation(userId, meal);
    }

    public MealTo update(MealTo mealTo) {
        log.info("MealRestController update: {}", mealTo);
        int userId = SecurityUtil.authUserId();
        Meal meal = MealsUtil.fromTo(mealTo);
        return service.update(userId, meal);
    }

    public void delete(int id) {
        log.info("MealRestController delete id: {}", id);
        int userId = SecurityUtil.authUserId();
        service.delete(userId, id);
    }

    public List<MealTo> getBetween(LocalDateTime from, LocalDateTime to) {
        log.info("MealRestController getBetween");
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        return service.getBetween(userId, caloriesPerDay, from, to);
    }
}