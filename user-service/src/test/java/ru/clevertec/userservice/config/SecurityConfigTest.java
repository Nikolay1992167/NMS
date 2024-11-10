package ru.clevertec.userservice.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void shouldReturnAuthenticationManagerFromAuthenticationConfiguration() throws Exception {
        // given
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager expectedValue = mock(AuthenticationManager.class);

        doReturn(expectedValue)
                .when(configuration)
                .getAuthenticationManager();

        // when
        AuthenticationManager actualValue = securityConfig.authenticationManager(configuration);

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnExpectedInstanceOfSecurityFilterChain() throws Exception {
        // given
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        HttpSecurity httpSecurityWhen = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        DefaultSecurityFilterChain expectedValue = mock(DefaultSecurityFilterChain.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.authorizeHttpRequests(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.exceptionHandling(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.sessionManagement(any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.addFilterBefore(any(), any())).thenReturn(httpSecurityWhen);
        when(httpSecurityWhen.build()).thenReturn(expectedValue);

        // when
        SecurityFilterChain actualValue = securityConfig.securityFilterChain(httpSecurity);

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }
}