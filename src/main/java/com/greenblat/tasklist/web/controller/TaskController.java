package com.greenblat.tasklist.web.controller;

import com.greenblat.tasklist.domain.task.Task;
import com.greenblat.tasklist.service.TaskService;
import com.greenblat.tasklist.web.dto.task.TaskDto;
import com.greenblat.tasklist.web.dto.validation.OnUpdate;
import com.greenblat.tasklist.web.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PutMapping
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto) {
        var updatedTask = taskService.update(taskMapper.toEntity(dto));
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable Long id) {
        var task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
