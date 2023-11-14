package com.greenblat.tasklist.web.controller;

import com.greenblat.tasklist.service.TaskService;
import com.greenblat.tasklist.service.UserService;
import com.greenblat.tasklist.web.dto.task.TaskDto;
import com.greenblat.tasklist.web.dto.user.UserDto;
import com.greenblat.tasklist.web.dto.validation.OnCreate;
import com.greenblat.tasklist.web.dto.validation.OnUpdate;
import com.greenblat.tasklist.web.mapper.TaskMapper;
import com.greenblat.tasklist.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "User API")
public class UserController {

    private final TaskService taskService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @PutMapping
    @Operation(summary = "Update user")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto dto) {
        var updatedUser = userService.update(userMapper.toEntity(dto));
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get UserDto by id")
    public UserDto getById(@PathVariable Long id) {
        var user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get all user tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable("id") Long userId) {
        var tasks = taskService.getAllByUserId(userId);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add task to user")
    public TaskDto createTask(@PathVariable("id") Long userId,
                              @Validated(OnCreate.class) @RequestBody TaskDto dto) {
        var task = taskMapper.toEntity(dto);
        var savedTask = taskService.create(task, userId);
        return taskMapper.toDto(savedTask);
    }
}
