package com.gv.user.management.mapper;

import com.gv.user.management.config.MapStructConfig;
import com.gv.user.management.dto.request.AdminUserUpdateRequest;
import com.gv.user.management.dto.request.UserRequest;
import com.gv.user.management.dto.response.UserResponse;
import com.gv.user.management.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {
    UserResponse map(User from);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "role", ignore = true)
    void fromDto(UserRequest dto, @MappingTarget User userDetails);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void fromDto(AdminUserUpdateRequest dto, @MappingTarget User userDetails);
}
