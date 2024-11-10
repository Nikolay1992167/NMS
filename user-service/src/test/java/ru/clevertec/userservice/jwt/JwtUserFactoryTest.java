package ru.clevertec.userservice.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.clevertec.userservice.enums.Status;
import ru.clevertec.userservice.model.Role;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.security.jwt.JwtUser;
import ru.clevertec.userservice.security.jwt.JwtUserFactory;
import ru.clevertec.userservice.util.testdata.UserTestData;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.userservice.util.Constants.ROLE_JOURNALIST;
import static ru.clevertec.userservice.util.Constants.ROLE_SUBSCRIBER;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;

class JwtUserFactoryTest {

    @Test
    void shouldCreateJwtUserFromUser() {
        // given
        User user = UserTestData.getJournalist();
        Role role1 = new Role();
        role1.setName(ROLE_SUBSCRIBER);
        Role role2 = new Role();
        role2.setName(ROLE_JOURNALIST);
        user.setRoles(Arrays.asList(role1, role2));

        // when
        JwtUser jwtUser = JwtUserFactory.create(user);
        List<GrantedAuthority> expectedAuthorities = Arrays.asList(
                new SimpleGrantedAuthority(role1.getName()),
                new SimpleGrantedAuthority(role2.getName())
        );

        // then
        assertThat(jwtUser)
                .hasFieldOrPropertyWithValue("id", ID_JOURNALIST)
                .hasFieldOrPropertyWithValue("username", user.getEmail())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("enabled", user.getStatus().equals(Status.ACTIVE))
                .hasFieldOrPropertyWithValue("authorities", expectedAuthorities);
    }
}