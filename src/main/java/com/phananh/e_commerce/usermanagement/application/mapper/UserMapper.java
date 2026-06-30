package com.phananh.e_commerce.usermanagement.application.mapper;

import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserResponse;
import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "credentials.username")
    @Mapping(target = "email", source = "info.email")
    @Mapping(target = "fullName", source = "info.fullName")
    @Mapping(target = "phoneNumber", source = "info.phoneNumber")
    @Mapping(target = "address", source = "info.address")
    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "isEnabled", source = "credentials.isEnabled")
    UserInfoResponse toUserInfoResponse(User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "credentials.username")
    @Mapping(target = "email", source = "info.email")
    @Mapping(target = "fullName", source = "info.fullName")
    @Mapping(target = "phoneNumber", source = "info.phoneNumber")
    @Mapping(target = "address", source = "info.address")
    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "isEnabled", source = "credentials.isEnabled")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "modifiedAt", source = "modifiedAt")
    @Mapping(target = "modifiedBy", source = "modifiedBy")
    UserResponse toResponse(User user);

    // helper method used in mapping expressions
    default Set<RoleName> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}


