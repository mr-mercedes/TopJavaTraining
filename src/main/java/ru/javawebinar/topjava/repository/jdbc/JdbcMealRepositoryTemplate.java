package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public abstract class JdbcMealRepositoryTemplate implements MealRepository {

    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected final SimpleJdbcInsert insertMeal;

    protected JdbcMealRepositoryTemplate(JdbcTemplate jdbcTemplate,
                                         NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertMeal = buildInsert(jdbcTemplate);
    }

    protected String tableName() {
        return "meal";
    }

    protected String idColumn() {
        return "id";
    }

    protected String userIdColumn() {
        return "user_id";
    }

    protected String dateTimeColumn() {
        return "date_time";
    }

    protected SimpleJdbcInsert buildInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName())
                .usingGeneratedKeyColumns(idColumn());
    }

    protected String query(String id) {
        return id;
    }

    protected Object dateTimeToDb(LocalDateTime ldt) {
        return Timestamp.valueOf(ldt);
    }

    protected LocalDateTime dateTimeFromDb(Timestamp ts) {
        return ts.toLocalDateTime();
    }

    protected RowMapper<Meal> rowMapper() {
        return (resultSet, __) -> {
            Meal meal = new Meal();
            meal.setId(resultSet.getInt(query(idColumn())));
            meal.setDescription(resultSet.getString("description"));
            meal.setCalories(resultSet.getInt("calories"));
            Timestamp ts = resultSet.getTimestamp(query(dateTimeColumn()));
            meal.setDateTime(dateTimeFromDb(ts));
            return meal;
        };
    }

    @Override
    public final Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue(idColumn(), meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue(dateTimeColumn(), dateTimeToDb(meal.getDateTime()))
                .addValue(userIdColumn(), userId);

        if (meal.isNew()) {
            Number newId = insertMeal.executeAndReturnKey(map);
            meal.setId(newId.intValue());
        } else {
            String sql = "UPDATE " + query(tableName()) +
                    " SET description=:description, calories=:calories, " + query(dateTimeColumn()) + "=:" + dateTimeColumn() +
                    " WHERE " + query(idColumn()) + "=:" + idColumn() + " AND " + query(userIdColumn()) + "=:" + userIdColumn();
            if (namedParameterJdbcTemplate.update(sql, map) == 0) return null;
        }
        return meal;
    }

    @Override
    public final boolean delete(int id, int userId) {
        String sql = "DELETE FROM " + query(tableName()) +
                " WHERE " + query(idColumn()) + "=? AND " + query(userIdColumn()) + "=?";
        return jdbcTemplate.update(sql, id, userId) != 0;
    }

    @Override
    public final Meal get(int id, int userId) {
        String sql = "SELECT * FROM " + query(tableName()) +
                " WHERE " + query(idColumn()) + "=? AND " + query(userIdColumn()) + "=?";
        List<Meal> meals = jdbcTemplate.query(sql, rowMapper(), id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public final List<Meal> getAll(int userId) {
        String sql = "SELECT * FROM " + query(tableName()) +
                " WHERE " + query(userIdColumn()) + "=? ORDER BY " + query(dateTimeColumn()) + " DESC";
        return jdbcTemplate.query(sql, rowMapper(), userId);
    }

    @Override
    public final List<Meal> getBetweenHalfOpen(LocalDateTime start, LocalDateTime end, int userId) {
        String sql = "SELECT * FROM " + query(tableName()) +
                " WHERE " + query(userIdColumn()) + "=? AND " + query(dateTimeColumn()) + " >= ? AND " + query(dateTimeColumn()) + " < ?" +
                " ORDER BY " + query(dateTimeColumn()) + " DESC";
        return jdbcTemplate.query(sql, rowMapper(), userId,
                Timestamp.valueOf(start),
                Timestamp.valueOf(end));
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Meal> getAllWithUser(int userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
