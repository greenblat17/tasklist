package com.greenblat.tasklist.web.mapper;

import com.greenblat.tasklist.domain.task.TaskImage;
import com.greenblat.tasklist.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}
