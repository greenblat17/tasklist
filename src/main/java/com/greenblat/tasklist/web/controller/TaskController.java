package com.greenblat.tasklist.web.controller;

import com.greenblat.tasklist.service.TaskService;
import com.greenblat.tasklist.web.dto.task.TaskDto;
import com.greenblat.tasklist.web.dto.validation.OnUpdate;
import com.greenblat.tasklist.web.mapper.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PutMapping
    @Operation(summary = "Update task")
    @PreAuthorize("canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto) {
        var updatedTask = taskService.update(taskMapper.toEntity(dto));
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ger TaskDto by id")
    @PreAuthorize("canAccessTask(#id)")
    public TaskDto getById(@PathVariable Long id) {
        var task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @PreAuthorize("canAccessTask(#id)")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
