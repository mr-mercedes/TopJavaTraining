package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
        return mealsMap.compute(userId, (id, userMeals) -> {
            ConcurrentMap<Integer, Meal> map = (userMeals != null)
                    ? (ConcurrentHashMap<Integer, Meal>) userMeals
                    : new ConcurrentHashMap<>();

            if (meal.isNew()) {
                int newId = counter.incrementAndGet();
                meal.setId(newId);
                map.putIfAbsent(newId, meal);
            } else {
                map.computeIfPresent(meal.getId(), (mid, old) -> meal);
            }
            return map;
        }).get(meal.getId());
    }

    @Override
    public boolean delete(int userId, int id) {
        return mealsMap.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return mealsMap.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).get(id);
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate from, LocalDate to) {
        return mealsMap.computeIfAbsent(userId, meal -> new HashMap<>())
                .values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), from, to))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll() {
        return mealsMap.values().stream().flatMap(meals -> meals.values().stream())
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

