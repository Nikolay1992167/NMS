package ru.clevertec.userservice.util.testdata;

import ru.clevertec.userservice.dto.response.RoleResponse;
import ru.clevertec.userservice.model.Role;

import java.util.List;

import static ru.clevertec.userservice.util.Constants.ROLE_JOURNALIST;
import static ru.clevertec.userservice.util.Constants.ROLE_SUBSCRIBER;
import static ru.clevertec.userservice.util.initdata.InitData.CREATED_AT_ROLE;
import static ru.clevertec.userservice.util.initdata.InitData.CREATED_BY_ROLE;
import static ru.clevertec.userservice.util.initdata.InitData.ID_ROLE;
import static ru.clevertec.userservice.util.initdata.InitData.ROLE_NAME_ADMIN;
import static ru.clevertec.userservice.util.initdata.InitData.UPDATED_AT_ROLE;
import static ru.clevertec.userservice.util.initdata.InitData.UPDATED_BY_ROLE;

public class RoleTestData {

    public static Role getJournalist() {
        return Role.builder()
                .id(ID_ROLE)
                .createdBy(CREATED_BY_ROLE)
                .updatedBy(UPDATED_BY_ROLE)
                .createdAt(CREATED_AT_ROLE)
                .updatedAt(UPDATED_AT_ROLE)
                .name(ROLE_JOURNALIST)
                .users(List.of())
                .build();
    }

    public static Role getSubscriber() {
        return Role.builder()
                .id(ID_ROLE)
                .createdBy(CREATED_BY_ROLE)
                .updatedBy(UPDATED_BY_ROLE)
                .createdAt(CREATED_AT_ROLE)
                .updatedAt(UPDATED_AT_ROLE)
                .name(ROLE_SUBSCRIBER)
                .users(List.of())
                .build();
    }

    public static Role getAdmin() {
        return Role.builder()
                .id(ID_ROLE)
                .createdBy(CREATED_BY_ROLE)
                .updatedBy(UPDATED_BY_ROLE)
                .createdAt(CREATED_AT_ROLE)
                .updatedAt(UPDATED_AT_ROLE)
                .name(ROLE_NAME_ADMIN)
                .users(List.of())
                .build();
    }

    public static RoleResponse getRoleResponse() {
        return RoleResponse.builder()
                .id(ID_ROLE)
                .name(ROLE_SUBSCRIBER)
                .build();
    }
}