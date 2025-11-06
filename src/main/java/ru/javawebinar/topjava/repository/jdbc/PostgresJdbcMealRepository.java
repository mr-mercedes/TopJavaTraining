package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.time.LocalDateTime;


@Repository
@Profile(Profiles.POSTGRES_DB)
public class PostgresJdbcMealRepository extends JdbcMealRepositoryTemplate<LocalDateTime> {

    public PostgresJdbcMealRepository(JdbcTemplate jdbcTemplate,
                                      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected SimpleJdbcInsert buildInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("public")
                .withTableName(tableName())
                .usingGeneratedKeyColumns(idColumn());
    }

    @Override
    public LocalDateTime dateTimeToDb(LocalDateTime ldt) {
        return ldt;
    }
}
