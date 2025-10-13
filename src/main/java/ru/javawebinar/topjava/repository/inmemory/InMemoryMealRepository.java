package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        int adminId = 1;
        int userId = 2;
        MealsUtil.userMeals.forEach(meal -> save(adminId, meal));
        MealsUtil.adminMeals.forEach(meal -> save(userId, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        mealsMap.compute(userId, (id, userMeals) -> {
            Map<Integer, Meal> map = (userMeals != null)
                    ? userMeals
                    : new ConcurrentHashMap<>();

            if (meal.isNew()) {
                int newId = counter.incrementAndGet();
                meal.setId(newId);
                map.put(newId, meal);
            } else {
                map.computeIfPresent(meal.getId(), (mid, old) -> meal);
            }
            return map;
        });
        return meal;
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
    public List<Meal> getBetween(int userId, LocalDateTime from, LocalDateTime to) {
        return mealsMap.computeIfAbsent(userId, meal -> new HashMap<>())
                .values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), from, to))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealsMap.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

