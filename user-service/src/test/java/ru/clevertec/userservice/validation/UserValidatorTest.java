package ru.clevertec.userservice.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exception.handling.exception.UniqueEmailException;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.util.testdata.UserTestData;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_SUBSCRIBER;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.ID_SUBSCRIBER;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldThrowExceptionWhenEmailIsNotUnique() {
        // given
        String userEmail = EMAIL_JOURNALIST;
        User user = UserTestData.getJournalist();
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> userValidator.checkUniqueEmail(userEmail))
                .isInstanceOf(UniqueEmailException.class)
                .hasMessage(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + userEmail);
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsUnique() {
        // given
        String userEmail = EMAIL_SUBSCRIBER;
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.empty());

        // when, then
        assertThatCode(() -> userValidator.checkUniqueEmail(userEmail)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNotUniqueForDifferentUserId() {
        // given
        String userEmail = EMAIL_JOURNALIST;
        User user = UserTestData.getJournalist();
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> userValidator.checkUniqueEmail(userEmail, ID_SUBSCRIBER))
                .isInstanceOf(UniqueEmailException.class)
                .hasMessage(ErrorMessage.UNIQUE_USER_EMAIL.getMessage() + userEmail);
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsUniqueUserId() {
        // given
        String userEmail = EMAIL_SUBSCRIBER;
        UUID userId = ID_JOURNALIST;
        User user = UserTestData.getJournalist();
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(user));

        // when, then
        assertThatCode(() -> userValidator.checkUniqueEmail(userEmail, userId)).doesNotThrowAnyException();
    }
}