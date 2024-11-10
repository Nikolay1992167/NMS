package ru.clevertec.util.testdata;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.clevertec.dto.request.JwtUser;

import java.util.Collections;

import static ru.clevertec.util.init.InitData.CREATED_BY;
import static ru.clevertec.util.init.InitData.EMAIL_JOURNALIST;
import static ru.clevertec.util.init.InitData.FIRST_NAME_JOURNALIST;
import static ru.clevertec.util.init.InitData.ID_ADMIN_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_AUTHOR_NEWS;
import static ru.clevertec.util.init.InitData.ID_JOURNALIST;
import static ru.clevertec.util.init.InitData.LAST_NAME_JOURNALIST;
import static ru.clevertec.util.init.InitData.PASSWORD_JOURNALIST;
import static ru.clevertec.util.init.InitData.ROLE_NAME_ADMIN;
import static ru.clevertec.util.init.InitData.ROLE_NAME_JOURNALIST;

public class JwtUserTestData {

    public static JwtUser getJwtUserAdmin() {
        return JwtUser.builder()
                .id(ID_ADMIN_FOR_IT)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .enabled(false)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(ROLE_NAME_ADMIN)))
                .build();
    }

    public static JwtUser getJwtUserValidationCommentAdmin() {
        return JwtUser.builder()
                .id(CREATED_BY)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .enabled(false)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(ROLE_NAME_ADMIN)))
                .build();
    }

    public static JwtUser getJwtUserValidationNews() {
        return JwtUser.builder()
                .id(ID_AUTHOR_NEWS)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .enabled(false)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(ROLE_NAME_JOURNALIST)))
                .build();
    }

    public static JwtUser getJwtUser() {
        return JwtUser.builder()
                .id(ID_JOURNALIST)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .enabled(false)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(ROLE_NAME_JOURNALIST)))
                .build();
    }
}