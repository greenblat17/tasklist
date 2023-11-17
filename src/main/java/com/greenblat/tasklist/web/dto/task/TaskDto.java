package com.greenblat.tasklist.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.greenblat.tasklist.domain.task.Status;
import com.greenblat.tasklist.web.dto.validation.OnCreate;
import com.greenblat.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {

    @NotNull(
            message = "Id must be not null",
            groups = OnUpdate.class
    )
    private Long id;

    @NotBlank(
            message = "Title must be not blank",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Title length must be smaller than 255 symbols",
            groups = {OnUpdate.class, OnCreate.class}
    )
    private String title;

    @Length(
            max = 255,
            message = "Description length must be smaller than 255 symbols",
            groups = {OnUpdate.class, OnCreate.class}
    )
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Status status;

    private LocalDateTime expirationDate;

    @JsonProperty(access = Access.READ_ONLY)
    private List<String> images;

}
