package ru.javawebinar.topjava.repository;

import java.util.List;
import java.util.Optional;

public interface InMemoryMealCRUDRepository<T, ID> {
    List<T> findAllMeals();

    Optional<T> createMeal(T entity);

    Optional<T> findById(ID id);

    Optional<T> updateMeal(ID id, T entity);

    void deleteMeal(ID id);
}
