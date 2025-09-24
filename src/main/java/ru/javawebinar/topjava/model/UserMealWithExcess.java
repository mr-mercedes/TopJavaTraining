package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;
    private final AtomicBoolean excessRef;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.excessRef = new AtomicBoolean(false); //заглушка для простого решения
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, AtomicBoolean excessRef) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excessRef = excessRef;
        this.excess = false; //заглушка для простого решения
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                ", excessRef=" + excessRef.get() +
                '}';
    }
}
