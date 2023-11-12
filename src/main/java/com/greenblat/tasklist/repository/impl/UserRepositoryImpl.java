package com.greenblat.tasklist.repository.impl;

import com.greenblat.tasklist.domain.exception.ResourceMappingException;
import com.greenblat.tasklist.domain.user.Role;
import com.greenblat.tasklist.domain.user.User;
import com.greenblat.tasklist.repository.DataSourceConfig;
import com.greenblat.tasklist.repository.UserRepository;
import com.greenblat.tasklist.repository.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private static final String FIND_BY_ID_SQL = """
            SELECT u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   ur.role as user_role_role,
                   t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            FROM users u
                LEFT JOIN users_roles ur on u.id = ur.user_id
                LEFT JOIN users_tasks ut on u.id = ut.user_id
                LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.id = ?""";

    private static final String FIND_BY_USERNAME_SQL = """
            SELECT u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   ur.role as user_role_role,
                   t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            FROM users u
                LEFT JOIN users_roles ur on u.id = ur.user_id
                LEFT JOIN users_tasks ut on u.id = ut.user_id
                LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.username = ?""";

    private static final String UPDATE_SQL = """
            UPDATE users
            SET name = ?,
                username = ?,
                password = ?
            WHERE id = ?""";

    private static final String CREATE_SQL = """
            INSERT INTO users (name, username, password)
            VALUES (?, ?, ?)""";

    private static final String INSERT_USER_ROLE_SQL = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?, ?)""";

    private static final String IS_TASK_OWNER_SQL = """
            SELECT exists(
                           SELECT 1
                           FROM users_tasks
                           WHERE user_id = ?
                             AND task_id = ?
                       )""";

    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?""";

    @Override
    public Optional<User> findById(Long id) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            statement.setLong(1, id);

            var resultSet = statement.executeQuery();
            return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while finding user by id.");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_USERNAME_SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, username);
            var resultSet = statement.executeQuery();
            return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while finding user by username.");
        }
    }

    @Override
    public void update(User user) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    @Override
    public void create(User user) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(CREATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while creating user.");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(INSERT_USER_ROLE_SQL)) {
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while inserting user role.");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(IS_TASK_OWNER_SQL))  {
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            var resultSet = statement.executeQuery();

            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while checking if user is task owner..");
        }
    }

    @Override
    public void delete(Long id) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while deleting user.");
        }
    }

}
