package com.greenblat.tasklist.web.dto.user;

import com.greenblat.tasklist.domain.task.Task;
import com.greenblat.tasklist.domain.user.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;

}
