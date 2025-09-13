package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collector;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 7, 0), "Ровно в startTime", 500),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 11, 59), "Минутка до endTime", 1200),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 12, 0), "Ровно в endTime", 800),
                new UserMeal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 15, 0), "После endTime", 1000)
        );
        System.out.println("Filtered by Cycles");
        List<UserMealWithExcess> mealsToCycles = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToCycles.forEach(System.out::println);
        System.out.println("\n");
        System.out.println("Filtered by Streams");
        List<UserMealWithExcess> mealsToStreams = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToStreams.forEach(System.out::println);
    }

    static class DayState {
        int sum;
        final AtomicBoolean excess = new AtomicBoolean(false);

        public int getSum() {
            return sum;
        }
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> filteredMeals = new ArrayList<>();
        Map<LocalDate, DayState> days = new HashMap<>();
        meals.forEach(meal -> {
            DayState st = days.computeIfAbsent(meal.getDateTime().toLocalDate(), k -> new DayState());
            st.sum += meal.getCalories();
            if (st.getSum() > caloriesPerDay) {
                st.excess.set(true);
            }
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredMeals.add(new UserMealWithExcess(meal, st.excess));
            }
        });
        return filteredMeals;
    }


    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final class Acc {
            final Map<LocalDate, DayState> days = new HashMap<>();
            final List<UserMeal> inWindow = new ArrayList<>();
        }
        return meals.stream().collect(Collector.of(
                Acc::new,
                ((acc, meal) -> {
                    LocalDate day = meal.getDateTime().toLocalDate();
                    DayState st = acc.days.computeIfAbsent(day, k -> new DayState());
                    st.sum += meal.getCalories();
                    if (st.sum > caloriesPerDay) {
                        st.excess.set(true);
                    }
                    if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                        acc.inWindow.add(meal);
                    }
                }),
                ((a1, a2) -> {
                    a2.days.forEach((day, st2) -> {
                        DayState st1 = a1.days.computeIfAbsent(day, k -> new DayState());
                        st1.sum += st2.sum;
                        if (st1.sum > caloriesPerDay) {
                            st1.excess.set(true);
                        }
                    });
                    a1.inWindow.addAll(a2.inWindow);
                    return a1;
                }),
                ((acc) -> {
                    List<UserMealWithExcess> res = new ArrayList<>(acc.inWindow.size());
                    acc.inWindow.forEach(m -> {
                        DayState st = acc.days.get(m.getDateTime().toLocalDate());
                        res.add(new UserMealWithExcess(m, st.excess));
                    });
                    return res;
                })
        ));
    }
}
