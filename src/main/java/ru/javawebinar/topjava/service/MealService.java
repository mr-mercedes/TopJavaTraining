package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<MealTo> getAll(int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(), caloriesPerDay);
    }

    public MealTo create(int userId, Meal meal) {
        Meal save = repository.save(userId, meal);
        return MealsUtil.createTo(save, true);
    }

    public MealTo get(int userId, int mealId) {
        Meal meal = repository.get(userId, mealId);
        ValidationUtil.checkNotFound(meal, "Meal with id " + mealId + " not found");
        return MealsUtil.createTo(meal, true);
    }

    public void update(int userId, Meal meal) {
        Meal updated = repository.save(userId, meal);
        ValidationUtil.checkNotFound(updated, "Meal with id " + updated.getId() + " not found");
    }

    public void delete(int userId, int mealId) {
        if (!repository.delete(userId, mealId)) {
            throw new NotFoundException("Meal with id " + mealId + " not found");
        }
    }

    public List<MealTo> getBetween(int userId, int caloriesPerDay, LocalDateTime from, LocalDateTime to) {
        List<Meal> meals = repository.getAll(userId, from.toLocalDate(), to.toLocalDate());
        return MealsUtil.getFilteredTos(meals, caloriesPerDay, from.toLocalTime(), to.toLocalTime());
    }
}