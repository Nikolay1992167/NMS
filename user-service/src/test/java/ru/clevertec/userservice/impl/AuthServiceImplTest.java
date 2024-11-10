package ru.clevertec.userservice.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import ru.clevertec.userservice.dto.request.JwtRequest;
import ru.clevertec.userservice.dto.response.JwtResponse;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.security.jwt.JwtTokenProvider;
import ru.clevertec.userservice.service.UserService;
import ru.clevertec.userservice.service.impl.AuthServiceImpl;
import ru.clevertec.userservice.util.testdata.JwtTestData;
import ru.clevertec.userservice.util.testdata.UserTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.initdata.InitData.REFRESH_TOKEN;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @Test
    void shouldReturnExpectedJwtResponseWhenLogin() {
        // given
        JwtRequest loginRequest = JwtTestData.getJwtRequest();
        User user = UserTestData.getJournalist();
        JwtResponse expectedResponse = JwtTestData.getJwtResponse();
        when(userService.findActiveUserByEmailOrThrowException(loginRequest.getEmail()))
                .thenReturn(user);
        when(userMapper.toJwtResponse(user))
                .thenReturn(expectedResponse);

        // when
        JwtResponse actualResponse = authService.login(loginRequest);

        // then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnExpectedJwtResponseWhenRefresh() {
        // given
        String refreshToken = REFRESH_TOKEN;
        JwtResponse expectedResponse = JwtTestData.getJwtResponse();
        when(jwtTokenProvider.refreshUserToken(refreshToken))
                .thenReturn(expectedResponse);

        // when
        JwtResponse actualResponse = authService.getRefreshToken(refreshToken);

        // then
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}