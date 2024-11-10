package ru.clevertec.userservice.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.security.jwt.JwtUser;
import ru.clevertec.userservice.security.jwt.JwtUserDetailsService;
import ru.clevertec.userservice.security.jwt.JwtUserFactory;
import ru.clevertec.userservice.service.UserService;
import ru.clevertec.userservice.util.testdata.UserTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_JOURNALIST;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private UserService userService;

    @Test
    void shouldLoadUserByUsername() {
        // given
        String emailUser = EMAIL_JOURNALIST;
        User user = UserTestData.getJournalist();
        JwtUser expectedJwtUser = JwtUserFactory.create(user);
        when(userService.findActiveUserByEmailOrThrowException(emailUser))
                .thenReturn(user);

        // when
        UserDetails actualJwtUser = jwtUserDetailsService.loadUserByUsername(emailUser);

        // then
        assertThat(actualJwtUser).isEqualTo(expectedJwtUser);
    }

    @Test
    void shouldThrowExceptionWhenStatusNotActive() {
        // given
        User user = UserTestData.getJournalist();
        when(userService.findActiveUserByEmailOrThrowException(user.getEmail()))
                .thenThrow(new UsernameNotFoundException("User not found!"));

        // when, then
        assertThatThrownBy(() -> jwtUserDetailsService.loadUserByUsername(user.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found!");
    }
}