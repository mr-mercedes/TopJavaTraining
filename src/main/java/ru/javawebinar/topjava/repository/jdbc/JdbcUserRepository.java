package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.JdbcEntityValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final JdbcEntityValidator VALIDATOR = new JdbcEntityValidator();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        VALIDATOR.validateOrThrow(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (
                namedParameterJdbcTemplate.update("""
                           UPDATE users
                                SET name=:name,
                                    email=:email,
                                    password=:password,
                                    enabled=:enabled,
                                    calories_per_day=:caloriesPerDay
                                WHERE id=:id
                        """, parameterSource) == 0) {
            return null;
        }
        syncRoles(user.id(), user.getRoles());
        return user;
    }

    private void syncRoles(int userId, Set<Role> newRoles) {
        Set<Role> existing = new HashSet<>(
                jdbcTemplate.query(
                        "SELECT role FROM user_role WHERE user_id = ?",
                        (rs, i) -> Role.valueOf(rs.getString(1)),
                        userId
                )
        );

        Set<Role> toAdd = new HashSet<>(newRoles);
        toAdd.removeAll(existing);

        Set<Role> toRemove = new HashSet<>(existing);
        toRemove.removeAll(newRoles);

        if (!toRemove.isEmpty()) {
            List<Role> list = new ArrayList<>(toRemove);
            jdbcTemplate.batchUpdate(
                    "DELETE FROM user_role WHERE user_id = ? AND role = ?",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, userId);
                            ps.setString(2, list.get(i).name());
                        }

                        @Override
                        public int getBatchSize() {
                            return list.size();
                        }
                    }
            );
        }

        if (!toAdd.isEmpty()) {
            List<Role> list = new ArrayList<>(toAdd);
            jdbcTemplate.batchUpdate(
                    "INSERT INTO user_role (user_id, role) VALUES (?, ?) " +
                            "ON CONFLICT (user_id, role) DO NOTHING",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, userId);
                            ps.setString(2, list.get(i).name());
                        }

                        @Override
                        public int getBatchSize() {
                            return list.size();
                        }
                    }
            );
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        String sql = """
                 SELECT * FROM users u
                 LEFT JOIN user_role ur on u.id = ur.user_id
                 WHERE id=?
                """;

        List<User> users = jdbcTemplate.query(sql, JdbcUserRepository::extractData, id);
        return DataAccessUtils.singleResult(users);
    }

    private static List<User> extractData(ResultSet rs) throws SQLException {
        Map<Integer, User> users = new LinkedHashMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            User u = users.get(id);
            if (u == null) {
                u = new User();
                u.setId(id);
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setEnabled(rs.getBoolean("enabled"));
                u.setRegistered(rs.getTimestamp("registered"));
                u.setCaloriesPerDay(rs.getInt("calories_per_day"));
                u.setRoles(new HashSet<>());
                users.put(id, u);
            }
            String roleStr = rs.getString("role");
            if (roleStr != null) {
                u.getRoles().add(Role.valueOf(roleStr));
            }
        }
        return users.values().stream().toList();
    }

    @Override
    public User getByEmail(String email) {
        String sql = """
                 SELECT * FROM users u
                 LEFT JOIN user_role ur on u.id = ur.user_id
                 WHERE email=?
                """;

        List<User> users = jdbcTemplate.query(sql, JdbcUserRepository::extractData, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        String sql = """
                 SELECT * FROM users u
                 LEFT JOIN user_role ur on u.id = ur.user_id
                 ORDER BY u.email
                """;

        return jdbcTemplate.query(sql, JdbcUserRepository::extractData);
    }
}
