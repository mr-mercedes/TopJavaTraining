package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.storage.InMemoryMealStorage;

public class StorageUtil extends InMemoryMealStorage {

    private static InMemoryMealStorage INSTANCE;

    private StorageUtil() {
    }

    public static InMemoryMealStorage connect() {
        if (INSTANCE == null) {
            INSTANCE = new StorageUtil();
        }
        return INSTANCE;
    }
}
