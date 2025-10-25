package ru.javawebinar.topjava.service;


import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class BaseTest {

    private static final Logger LOG = Logger.getLogger(BaseTest.class.getName());
    private final static Map<String, Long> TIMES = new ConcurrentHashMap<>();
    private static String currentTestClass = null;


    @Rule
    public final TestRule stopWatch = new Stopwatch() {

        @Override
        protected void finished(long nanos, Description description) {
            long ms = TimeUnit.NANOSECONDS.toMillis(nanos);
            String method = description.getMethodName();
            currentTestClass = description.getTestClass().getSimpleName();
            String className = String.format("%s#%s", currentTestClass, method);
            TIMES.put(method, ms);
            LOG.info(String.format("TEST %-5s : %6d ms", className, ms));
        }
    };

    @ClassRule
    public static final ExternalResource summary = new ExternalResource() {

        @Override
        protected void before() {
            TIMES.clear();
        }

        @Override
        protected void after() {
            if (currentTestClass == null) {
                currentTestClass = "UnknownTestClass";
            }

            LOG.info("========= TEST TIME SUMMARY (ms) =========");
            LOG.info("Class: " + currentTestClass);
            TIMES.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(e -> LOG.info(String.format("%-5s : %6d", e.getKey(), e.getValue())));

            long total = TIMES.values().stream().mapToLong(Long::longValue).sum();
            double avg = TIMES.isEmpty() ? 0.0 : (total * 1.0 / TIMES.size());
            LOG.info("------------------------------------------");
            LOG.info(String.format("TOTAL: %d ms,  AVERAGE: %.1f ms", total, avg));
            LOG.info("==========================================");
        }
    };
}
