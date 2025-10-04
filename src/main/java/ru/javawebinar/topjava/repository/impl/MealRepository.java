package ru.javawebinar.topjava.repository.impl;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.CrudRepository;
import ru.javawebinar.topjava.util.annotation.IdGenerator;
import ru.javawebinar.topjava.util.annotation.Id;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealRepository implements CrudRepository<Meal, Integer>, IdGenerator {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    public static final List<Meal> demoMeals = Arrays.asList(
            new Meal(1, LocalDateTime.of(2025, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(2, LocalDateTime.of(2025, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(3, LocalDateTime.of(2025, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(4, LocalDateTime.of(2025, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(5, LocalDateTime.of(2025, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(6, LocalDateTime.of(2025, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(7, LocalDateTime.of(2025, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public MealRepository() {
        startFill(meals);
    }


    @Override
    public boolean exists(Integer id) {
        return meals.get(id) != null;
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal create(Meal entity) {
        apply(entity);
        return meals.put(entity.getId(), entity);
    }

    @Override
    public Meal update(Meal entity) {
        return meals.replace(entity.getId(), entity);
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        return Optional.ofNullable(meals.get(id));
    }

    @Override
    public void delete(Integer id) {
        boolean exists = exists(id);
        if (!exists) throw new RuntimeException(String.format("Meal with id: %d not found", id));
        meals.remove(id);
    }

    @Override
    public void apply(Object target) {
        Class<?> c = target.getClass();
        for (Field f : c.getDeclaredFields()) {
            Id ann = f.getAnnotation(Id.class);
            if (ann == null) continue;

            f.setAccessible(true);
            try {
                long next = ID_GENERATOR.incrementAndGet();

                if (f.getType() == int.class) {
                    f.setInt(target, Math.toIntExact(next));
                } else if (f.getType() == long.class) {
                    f.setLong(target, next);
                } else if (f.getType() == Integer.class) {
                    f.set(target, Math.toIntExact(next));
                } else if (f.getType() == Long.class) {
                    f.set(target, next);
                } else {
                    throw new IllegalArgumentException("@Id поддерживает только int/long и Integer/Long: " + f);
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startFill(Map<Integer, Meal> meals) {
        for (Meal meal : demoMeals) {
            meals.put(meal.getId(), meal);
        }
    }
}
