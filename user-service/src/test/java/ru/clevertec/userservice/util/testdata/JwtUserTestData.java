package ru.clevertec.userservice.util.testdata;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.clevertec.userservice.security.jwt.JwtUser;

import java.util.Collections;

import static ru.clevertec.userservice.util.Constants.ROLE_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.FIRST_NAME_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.LAST_NAME_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.PASSWORD_JOURNALIST;


public class JwtUserTestData {

    public static JwtUser getJwtUser() {
        return JwtUser.builder()
                .id(ID_JOURNALIST)
                .firstName(FIRST_NAME_JOURNALIST)
                .lastName(LAST_NAME_JOURNALIST)
                .password(PASSWORD_JOURNALIST)
                .email(EMAIL_JOURNALIST)
                .enabled(false)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(ROLE_JOURNALIST)))
                .build();
    }
}