package ru.clevertec.userservice.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Test
    void shouldReturnExpectedInstanceOfBCryptPasswordEncoder() {
        // given
        Class<BCryptPasswordEncoder> expectedClass = BCryptPasswordEncoder.class;

        // when
        PasswordEncoder actualClass = applicationConfig.passwordEncoder();

        // then
        assertThat(actualClass).isInstanceOf(expectedClass);
    }
}