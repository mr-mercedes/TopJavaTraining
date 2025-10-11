package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        int defaultUserId = 1;
        MealsUtil.meals.forEach(meal -> save(defaultUserId, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
            return meal;
        }
        mealsMap.computeIfPresent(userId, (id, userMeals) -> {
            userMeals.computeIfPresent(meal.getId(), (mealId, oldMeal) -> meal);
            return userMeals;
        });
        return get(userId, meal.getId());
    }

    @Override
    public boolean delete(int userId, int id) {
        return mealsMap.getOrDefault(userId, new HashMap<>()).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return mealsMap.getOrDefault(userId, new HashMap<>()).get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return mealsMap.getOrDefault(userId, new HashMap<>())
                .values().stream()
                .sorted()
                .collect(Collectors.toList());
    }
}

