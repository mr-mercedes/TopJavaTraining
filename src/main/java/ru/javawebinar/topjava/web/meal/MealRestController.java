package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        return service.getAll(userId, caloriesPerDay);
    }

    public MealTo getById(int id) {
        int userId = SecurityUtil.authUserId();
        return service.get(userId, id);
    }

    public MealTo createWithLocation(MealTo mealTo) {
        int userId = SecurityUtil.authUserId();
        Meal meal = MealsUtil.fromTo(mealTo);
        return service.createWithLocation(userId, meal);
    }

    public MealTo update(MealTo mealTo) {
        int userId = SecurityUtil.authUserId();
        Meal meal = MealsUtil.fromTo(mealTo);
        return service.update(userId, meal);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        service.delete(userId, id);
    }

    public List<MealTo> getBetween(String startDate, String endDate, String startTime, String endTime) {
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        LocalDateTime start = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse(startTime));
        LocalDateTime end = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse(endTime));
        return service.getBetween(userId, caloriesPerDay, start, end);
    }
}