package com.greenblat.tasklist.web.mapper;

import com.greenblat.tasklist.domain.user.User;
import com.greenblat.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

}
