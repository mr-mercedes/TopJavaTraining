package ru.javawebinar.topjava.service;


import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    private static final Map<String, Long> times = new HashMap<>();
    private static final String title = "\n========= TEST TIME SUMMARY (ms) =========\n";
    private static final int offsetPosition = title.length();
    private static String currentTestClass;

    @Rule
    public final TestRule stopWatch = new Stopwatch() {

        @Override
        protected void finished(long nanos, Description description) {
            long ms = TimeUnit.NANOSECONDS.toMillis(nanos);
            String method = description.getMethodName();
            currentTestClass = description.getTestClass().getSimpleName();
            String className = String.format("%s#%s", currentTestClass, method);
            times.put(method, ms);
            log.info(String.format("\nTEST " + getFormatPattern(className.length()), className, ms));
        }
    };

    private static String getFormatPattern(int methodNameLength) {
        int delimiterLength = 3;
        int baseOffset = offsetPosition - methodNameLength;
        int resultLength = String.valueOf((baseOffset)).length();
        int offset = baseOffset - String.valueOf((resultLength)).length() - delimiterLength;
        return "%s : % " + offset + "d";
    }

    @ClassRule
    public static final ExternalResource summary = new ExternalResource() {

        @Override
        protected void after() {
            if (currentTestClass == null) {
                currentTestClass = "UnknownTestClass";
            }

            String logs = times.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .map(e -> {
                        String format = getFormatPattern(e.getKey().length());
                        return String.format(format, e.getKey(), e.getValue());
                    })
                    .collect(Collectors.joining("\n"));
            log.info("{}Class: {}\n{}", title, currentTestClass, logs);
        }
    };
}
