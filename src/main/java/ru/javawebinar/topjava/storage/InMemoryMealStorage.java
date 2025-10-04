package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealCRUDRepository;
import ru.javawebinar.topjava.util.annotation.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMealStorage implements InMemoryMealCRUDRepository<Meal, Integer> {

    protected InMemoryMealStorage() {
    }

    final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public List<Meal> findAllMeals() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Optional<Meal> createMeal(Meal entity) {
        IdGenerator.apply(entity);
        return Optional.ofNullable(meals.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        return Optional.ofNullable(meals.getOrDefault(id, null));
    }

    @Override
    public Optional<Meal> updateMeal(Integer id, Meal entity) {
        Meal meal = meals.getOrDefault(id, null);
        if (meal == null) throw new RuntimeException(String.format("Meal with id: %d not found", id));
        Meal newMeal = new Meal(id, entity.getDateTime(), entity.getDescription(), entity.getCalories());
        return Optional.ofNullable(meals.replace(id, newMeal));
    }

    @Override
    public void deleteMeal(Integer id) {
        Meal meal = meals.getOrDefault(id, null);
        if (meal == null) throw new RuntimeException(String.format("Meal with id: %d not found", id));
        meals.remove(id);
    }
}
