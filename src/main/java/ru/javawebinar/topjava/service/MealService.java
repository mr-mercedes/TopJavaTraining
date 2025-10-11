package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return new ArrayList<>(MealsUtil.getTos(repository.getAll(userId), caloriesPerDay));
    }

    public MealTo createWithLocation(int userId, Meal meal) {
        Meal save = repository.save(userId, meal);
        return MealsUtil.createTo(save, true);
    }

    public MealTo get(int userId, int mealId) {
        Meal meal = repository.get(userId, mealId);
        ValidationUtil.checkNotFound(meal, "Meal with id " + mealId + " not found");
        return MealsUtil.createTo(meal, true);
    }

    public MealTo update(int userId, Meal meal) {
        Meal updated = repository.save(userId, meal);
        return MealsUtil.createTo(updated, true);
    }

    public void delete(int userId, int mealId) {
        repository.delete(userId, mealId);
    }

    public List<MealTo> getBetween(int userId, int caloriesPerDay, LocalDateTime from, LocalDateTime to) {
        return MealsUtil.getFilteredTos(repository.getAll(userId), caloriesPerDay, from.toLocalTime(), to.toLocalTime());
    }
}