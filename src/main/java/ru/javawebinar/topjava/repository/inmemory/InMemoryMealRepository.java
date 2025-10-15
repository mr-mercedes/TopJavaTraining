package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final Function<Integer, Map<Integer, Meal>> NEW_MEAL_MAP = k -> new ConcurrentHashMap<>();

    {
        int adminId = 1;
        int userId = 2;
        MealsUtil.userMeals.forEach(meal -> save(adminId, meal));
        MealsUtil.adminMeals.forEach(meal -> save(userId, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        AtomicReference<Meal> mealRef = new AtomicReference<>();
        mealsMap.compute(userId, (id, userMeals) -> {
            Map<Integer, Meal> map = (userMeals != null)
                    ? userMeals
                    : new ConcurrentHashMap<>();
            if (meal.isNew()) {
                int newId = counter.incrementAndGet();
                meal.setId(newId);
                map.put(newId, meal);
                mealRef.set(meal);
                return map;
            }

            map.computeIfPresent(meal.getId(), (mid, old) -> {
                mealRef.set(meal);
                return meal;
            });
            return map;
        });
        return mealRef.get();
    }

    @Override
    public boolean delete(int userId, int id) {
        return mealsMap.computeIfAbsent(userId, NEW_MEAL_MAP).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return mealsMap.computeIfAbsent(userId, NEW_MEAL_MAP).get(id);
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDateTime from, LocalDateTime to) {
        return mealsMap.computeIfAbsent(userId, NEW_MEAL_MAP)
                .values().stream()
                .filter(meal -> DateTimeUtil.isBetweenDates(meal.getDate(), from.toLocalDate(), to.toLocalDate()))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealsMap.computeIfAbsent(userId, NEW_MEAL_MAP).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

