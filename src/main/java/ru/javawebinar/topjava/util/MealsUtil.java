package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.impl.MealRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<MealTo> mealsTo = filteredByStreams(MealRepository.demoMeals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, int caloriesPerDay) {
        return filteredByStreams(meals, null, null, caloriesPerDay);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        final boolean noTimeFilter = (startTime == null && endTime == null);
        final LocalTime start = (startTime != null) ? startTime : LocalTime.MIN;
        final LocalTime end = (endTime != null) ? endTime : LocalTime.MAX;

        return meals.stream()
                .filter(m -> noTimeFilter ||
                        TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), start, end))
                .map(m -> createTo(
                        m,
                        caloriesSumByDate.get(m.getDate()) > caloriesPerDay
                ))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
