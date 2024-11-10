package ru.clevertec.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.userservice.dto.request.UserRegisterRequest;
import ru.clevertec.userservice.dto.request.UserUpdateRequest;
import ru.clevertec.userservice.dto.response.JwtResponse;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.enums.Status;
import ru.clevertec.userservice.mapper.service.JwtTokenMapper;
import ru.clevertec.userservice.mapper.service.PasswordMapper;
import ru.clevertec.userservice.mapper.service.RoleMapper;
import ru.clevertec.userservice.model.User;

import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {RoleMapper.class, PasswordMapper.class, JwtTokenMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", source = "roleName", qualifiedByName = "setRolesList")
    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    User fromRequest(UserRegisterRequest source, Status status, String roleName);

    @Mapping(target = "updatedBy", source = "userId")
    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    void update(@MappingTarget User target, UserUpdateRequest source, UUID userId);

    @Mapping(target = "password", source = "source.password", qualifiedByName = "encodePassword")
    UserResponse toResponse(User source);

    @Mapping(target = "accessToken", source = "user", qualifiedByName = "createAccessToken")
    @Mapping(target = "refreshToken", source = "user", qualifiedByName = "createRefreshToken")
    JwtResponse toJwtResponse(User user);
}