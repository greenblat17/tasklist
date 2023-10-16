package com.greenblat.tasklist.web.controller;

import com.greenblat.tasklist.service.AuthService;
import com.greenblat.tasklist.service.UserService;
import com.greenblat.tasklist.web.dto.auth.JwtRequest;
import com.greenblat.tasklist.web.dto.auth.JwtResponse;
import com.greenblat.tasklist.web.dto.user.UserDto;
import com.greenblat.tasklist.web.dto.validation.OnCreate;
import com.greenblat.tasklist.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        var createdUser = userService.create(userMapper.toEntity(userDto));
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refrsh")
    public JwtResponse refresh(@RequestBody String token) {
        return authService.refresh(token);
    }
}
