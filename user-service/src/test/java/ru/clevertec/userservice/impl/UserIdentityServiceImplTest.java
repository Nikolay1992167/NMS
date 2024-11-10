package ru.clevertec.userservice.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.service.impl.UserIdentityServiceImpl;
import ru.clevertec.userservice.util.testdata.UserTestData;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;

@ExtendWith(MockitoExtension.class)
class UserIdentityServiceImplTest {

    @InjectMocks
    private UserIdentityServiceImpl userIdentityService;

    @Mock
    private UserRepository userRepository;


    @Test
    void shouldReturnExpectedValue() {
        // given
        UUID userID = ID_JOURNALIST;
        User expected = UserTestData.getJournalist();
        when(userRepository.findUserFetchRolesById(userID))
                .thenReturn(Optional.ofNullable(expected));

        // when
        User actual = userIdentityService.getUserOrThrowException(userID);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnThrowExceptionWhenUserNotFound() {
        // given
        UUID userId = ID_JOURNALIST;

        // when, then
        assertThatThrownBy(() -> userIdentityService.getUserOrThrowException(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
    }
}