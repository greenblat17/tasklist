package com.greenblat.tasklist.service.impl;

import com.greenblat.tasklist.domain.exception.ResourceNotFoundException;
import com.greenblat.tasklist.domain.task.Status;
import com.greenblat.tasklist.domain.task.Task;
import com.greenblat.tasklist.domain.task.TaskImage;
import com.greenblat.tasklist.repository.TaskRepository;
import com.greenblat.tasklist.service.ImageService;
import com.greenblat.tasklist.service.TaskService;
import com.greenblat.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllSoonTasks(Duration duration) {
        return taskRepository.findByDateBetween(
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().plus(duration))
        );
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", key = "#task.id")
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);

        var user = userService.getById(userId);
        user.getTasks().add(task);
        userService.update(user);

        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.findById(id)
                .ifPresentOrElse(
                        (task) -> taskRepository.deleteById(id),
                        () -> {
                            throw new ResourceNotFoundException("Task not found");
                        }

                );
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void uploadImage(Long id, TaskImage image) {
        String filename = imageService.upload(image);

        var task = getById(id);
        task.getImages().add(filename);
        taskRepository.save(task);
    }
}
