package com.greenblat.tasklist.web.mapper;

import com.greenblat.tasklist.domain.task.Task;
import com.greenblat.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
