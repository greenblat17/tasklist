package com.greenblat.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenblat.tasklist.domain.task.Task;
import com.greenblat.tasklist.domain.user.Role;
import com.greenblat.tasklist.web.dto.validation.OnCreate;
import com.greenblat.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {

    @NotNull(
            message = "Id must be not null",
            groups = OnUpdate.class
    )
    private Long id;

    @NotBlank(
            message = "Name must be not blank",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Name length must be smaller than 255 symbols",
            groups = {OnUpdate.class, OnCreate.class}
    )
    private String name;

    @NotBlank(
            message = "Username must be not blank",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Username length must be smaller than 255 symbols",
            groups = {OnUpdate.class, OnCreate.class}
    )
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(
            message = "Password must be not blank",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(
            message = "Password must be not blank",
            groups = OnCreate.class
    )
    private String passwordConfirmation;

}
