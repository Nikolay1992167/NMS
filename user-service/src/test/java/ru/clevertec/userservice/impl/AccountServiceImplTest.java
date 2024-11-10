package ru.clevertec.userservice.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.security.jwt.JwtUser;
import ru.clevertec.userservice.service.UserIdentityService;
import ru.clevertec.userservice.service.impl.AccountServiceImpl;
import ru.clevertec.userservice.util.testdata.JwtUserTestData;
import ru.clevertec.userservice.util.testdata.UserTestData;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private UserIdentityService userIdentityService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Test
    void shouldReturnExpectedUserResponse() {
        // given
        UUID userId = ID_JOURNALIST;
        User user = UserTestData.getJournalist();
        UserResponse expected = UserTestData.getUserResponse();
        JwtUser jwtUser = JwtUserTestData.getJwtUser();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        when(userIdentityService.getUserOrThrowException(userId))
                .thenReturn(user);
        when(userMapper.toResponse(user))
                .thenReturn(expected);
        SecurityContextHolder.setContext(securityContext);

        // when
        UserResponse actual = accountService.getUserData();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}