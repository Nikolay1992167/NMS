package ru.clevertec.userservice.utiltest;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.security.jwt.JwtUser;
import ru.clevertec.userservice.util.AuthUtil;
import ru.clevertec.userservice.util.testdata.JwtUserTestData;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;

@ExtendWith(MockitoExtension.class)
class AuthUtilTest {

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Nested
    class GetUserId {

        @Test
        void shouldReturnExpectedId() {
            // given
            UUID expectedId = ID_JOURNALIST;
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            UUID actualId = AuthUtil.getUserId();

            //then
            assertThat(actualId).isEqualTo(expectedId);
        }

        @Test
        void shouldReturnReturnThrowException() {
            // given
            when(authentication.getPrincipal())
                    .thenReturn(new Object());
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when, then
            assertThatThrownBy(AuthUtil::getUserId)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining(ErrorMessage.ERROR_EXTRACTION.getMessage());
        }
    }
}