package ru.clevertec.util.testdata;

import ru.clevertec.dto.response.RoleResponse;

import static ru.clevertec.util.init.InitData.ID_ROLE;
import static ru.clevertec.util.init.InitData.ROLE_NAME_ADMIN;
import static ru.clevertec.util.init.InitData.ROLE_NAME_JOURNALIST;
import static ru.clevertec.util.init.InitData.ROLE_NAME_SUBSCRIBER;

public class RoleTestData {

    public static RoleResponse getRoleResponseSubscriber() {
        return RoleResponse.builder()
                .id(ID_ROLE)
                .name(ROLE_NAME_SUBSCRIBER)
                .build();
    }

    public static RoleResponse getRoleResponseAdmin() {
        return RoleResponse.builder()
                .id(ID_ROLE)
                .name(ROLE_NAME_ADMIN)
                .build();
    }

    public static RoleResponse getRoleResponseJournalist() {
        return RoleResponse.builder()
                .id(ID_ROLE)
                .name(ROLE_NAME_JOURNALIST)
                .build();
    }
}