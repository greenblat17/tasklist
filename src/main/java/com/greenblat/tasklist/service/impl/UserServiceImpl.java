package com.greenblat.tasklist.service.impl;

import com.greenblat.tasklist.domain.exception.ResourceNotFoundException;
import com.greenblat.tasklist.domain.user.Role;
import com.greenblat.tasklist.domain.user.User;
import com.greenblat.tasklist.repository.UserRepository;
import com.greenblat.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        return user;
    }

    @Override
    @Transactional
    public User create(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(e -> {
                    throw new IllegalStateException("User already exists");
                });

        var password = user.getPassword();
        if (!password.equals(user.getPasswordConfirmation())) {
             throw new IllegalStateException("Password and password confirmation do not match");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.create(user);

        var usersRole = Role.ROLE_USER;
        userRepository.insertUserRole(user.getId(), usersRole);

        var roles = Set.of(usersRole);
        user.setRoles(roles);

        return user;
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(id);
    }
}
