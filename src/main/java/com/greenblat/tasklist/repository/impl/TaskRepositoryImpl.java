package com.greenblat.tasklist.repository.impl;

import com.greenblat.tasklist.domain.exception.ResourceMappingException;
import com.greenblat.tasklist.domain.task.Task;
import com.greenblat.tasklist.repository.DataSourceConfig;
import com.greenblat.tasklist.repository.TaskRepository;
import com.greenblat.tasklist.repository.mapper.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;

    private static final String FIND_BY_ID_SQL = """
            SELECT t.id              AS task_id,
                   t.title           AS task_title,
                   t.description     AS task_description,
                   t.expiration_date AS task_expiration_date,
                   t.status          AS task_status
            FROM tasks t
            WHERE id = ?
            """;
    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT t.id              AS task_id,
                   t.title           AS task_title,
                   t.description     AS task_description,
                   t.expiration_date AS task_expiration_date,
                   t.status          AS task_status
            FROM tasks t
                     JOIN tasklist.users_tasks ut on t.id = ut.task_id
            WHERE id = ?
            """;
    private static final String ASSIGN_SQL = """
            INSERT INTO users_tasks (task_id, user_id)
            VALUES (?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE tasks
            SET title           = ?,
                description     = ?,
                expiration_date = ?,
                status          = ?
            WHERE id = ?
            """;
    private static final String CREATE_SQL = """
            INSERT INTO tasks (title, description, expiration_date, status)
            VALUES (?, ?, ?, ?)  
            """;
    private static final String DELETE_SQL = """
            DELETE FROM tasks
            WHERE id = ?
            """;

    @Override
    public Optional<Task> findById(Long id) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            return Optional.ofNullable(TaskRowMapper.mapRow(resultSet));
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding task by id");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {

            statement.setLong(1, userId);
            var resultSet = statement.executeQuery();

            return TaskRowMapper.mapRows(resultSet);
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding all tasks by user id");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(ASSIGN_SQL)) {

            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while assigning task to user");
        }
    }

    @Override
    public void update(Task task) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.setLong(5, task.getId());


            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating task");
        }
    }

    @Override
    public void create(Task task) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(CREATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                task.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating task");
        }
    }

    @Override
    public void delete(Long id) {
        try (var connection = dataSourceConfig.getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting task");
        }
    }

}
