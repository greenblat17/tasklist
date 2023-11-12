package com.greenblat.tasklist.repository.mapper;

import com.greenblat.tasklist.domain.task.Status;
import com.greenblat.tasklist.domain.task.Task;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRowMapper {

    @SneakyThrows
    public static Task mapRow(ResultSet rs) {
        if (rs.next()) {
            var task = new Task();
            task.setId(rs.getLong("task_id"));
            task.setTitle(rs.getString("task_title"));
            task.setDescription(rs.getString("task_description"));
            task.setStatus(Status.valueOf(rs.getString("task_status")));

            var expirationDate = rs.getTimestamp("task_expiration_date");
            Optional.ofNullable(expirationDate)
                    .ifPresent(date -> task.setExpirationDate(expirationDate.toLocalDateTime()));

            return task;
        }
        return null;
    }

    @SneakyThrows
    public static List<Task> mapRows(ResultSet rs) {
        List<Task> tasks = new ArrayList<>();
        while (rs.next()) {
            var task = new Task();
            task.setId(rs.getLong("task_id"));
            if (!rs.wasNull()) {
                task.setTitle(rs.getString("task_title"));
                task.setDescription(rs.getString("task_description"));
                task.setStatus(Status.valueOf(rs.getString("task_status")));

                var expirationDate = rs.getTimestamp("task_expiration_date");
                Optional.ofNullable(expirationDate)
                        .ifPresent(date -> task.setExpirationDate(expirationDate.toLocalDateTime()));

                tasks.add(task);
            }
        }
        return tasks;
    }
}
