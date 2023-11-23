package com.greenblat.tasklist.service.impl;

import com.greenblat.tasklist.domain.MailType;
import com.greenblat.tasklist.domain.task.Task;
import com.greenblat.tasklist.domain.user.User;
import com.greenblat.tasklist.service.MailService;
import com.greenblat.tasklist.service.Reminder;
import com.greenblat.tasklist.service.TaskService;
import com.greenblat.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration duration = Duration.ofHours(1);

    @Override
    @Scheduled(cron = "0 * * * * *")
    public void remindForTask() {
        List<Task> tasks =  taskService.getAllSoonTasks(duration);
        tasks.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            Properties properties = setTaskProperties(task);
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });
    }

    private Properties setTaskProperties(Task task) {
        var properties = new Properties();
        properties.setProperty("task.title", task.getTitle());
        properties.setProperty("task.description", task.getTitle());
        return properties;
    }

}
