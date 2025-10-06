package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements CrudRepository<Meal, Integer> {

    private final AtomicInteger idGenerator = new AtomicInteger(0);
    final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    {
        Arrays.asList(
                new Meal(1, LocalDateTime.of(2025, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2025, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2025, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2025, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(5, LocalDateTime.of(2025, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(6, LocalDateTime.of(2025, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(7, LocalDateTime.of(2025, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ).forEach(this::create);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal create(Meal entity) {
        int id = idGenerator.incrementAndGet();
        Meal meal = entity.withId(id);
        meals.put(id, meal);
        return meal;
    }

    @Override
    public Meal update(Meal entity) {
        Integer id = entity.getId();
        return meals.computeIfPresent(id, (k, v) -> entity);
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        return Optional.ofNullable(meals.get(id));
    }

    @Override
    public void delete(Integer id) {
        meals.remove(id);
    }
}
