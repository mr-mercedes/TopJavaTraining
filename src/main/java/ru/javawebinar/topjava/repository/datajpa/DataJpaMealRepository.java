package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudUserRepository userRepository;
    private final CrudMealRepository mealRepository;

    public DataJpaMealRepository(CrudMealRepository mealRepository, CrudUserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getReferenceById(userId));
        if (meal.isNew()) {
            return mealRepository.save(meal);
        }
        return mealRepository.findByIdAndUserId(meal.id(), userId)
                .map(m -> mealRepository.save(meal))
                .orElse(null);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepository.findByIdAndUserId(id, userId).orElse(null);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return mealRepository.findByIdWithUser(id, userId).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAllByUserId(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.getBetweenHalfOpen(userId, startDateTime, endDateTime);
    }
}
