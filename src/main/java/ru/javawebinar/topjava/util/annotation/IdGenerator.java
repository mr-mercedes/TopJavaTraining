package ru.javawebinar.topjava.util.annotation;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    public IdGenerator() {
    }

    public static void apply(Object target) {
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
}
