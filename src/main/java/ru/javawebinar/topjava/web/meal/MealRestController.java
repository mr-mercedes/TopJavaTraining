package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll(int userId) {
        log.info("MealRestController getAll");
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();
        return service.getAll(userId, caloriesPerDay);
    }

    public Meal getById(int id) {
        log.info("MealRestController getById: {}", id);
        int userId = SecurityUtil.authUserId();
        return service.get(userId, id);
    }

    public Meal create(Meal meal) {
        log.info("MealRestController create: {}", meal);
        int userId = SecurityUtil.authUserId();
        checkIsNew(meal);
        return service.create(userId, meal);
    }

    public void update(Meal meal, int id) {
        log.info("MealRestController update: {} with id={}", meal, id);
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        service.update(userId, meal);
    }

    public void delete(int id) {
        log.info("MealRestController delete id: {}", id);
        int userId = SecurityUtil.authUserId();
        service.delete(userId, id);
    }

    public List<MealTo> getBetween(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        log.info("MealRestController getBetween");
        int userId = SecurityUtil.authUserId();
        int caloriesPerDay = SecurityUtil.authUserCaloriesPerDay();

        LocalDateTime from = LocalDateTime.of(
                fromDate == null
                        ? LocalDate.MIN
                        : fromDate,
                fromTime == null
                        ? LocalTime.MIN
                        : fromTime);
        LocalDateTime to = LocalDateTime.of(
                toDate == null
                        ? LocalDate.MAX
                        : toDate,
                toTime == null
                        ? LocalTime.MAX
                        : toTime);

        return service.getBetween(userId, caloriesPerDay, from, to);
    }
}