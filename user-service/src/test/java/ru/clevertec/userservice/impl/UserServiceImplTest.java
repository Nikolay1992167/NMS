package ru.clevertec.userservice.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.exception.handling.exception.InformationChangeStatusUserException;
import ru.clevertec.exception.handling.exception.NoSuchUserEmailException;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.userservice.dto.request.UserRegisterRequest;
import ru.clevertec.userservice.dto.request.UserUpdateRequest;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.enums.Status;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.Role;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.security.jwt.JwtUser;
import ru.clevertec.userservice.service.UserIdentityService;
import ru.clevertec.userservice.service.impl.UserServiceImpl;
import ru.clevertec.userservice.util.testdata.JwtUserTestData;
import ru.clevertec.userservice.util.testdata.UserTestData;
import ru.clevertec.userservice.validation.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.userservice.util.Constants.ROLE_JOURNALIST;
import static ru.clevertec.userservice.util.Constants.ROLE_SUBSCRIBER;
import static ru.clevertec.userservice.util.initdata.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static ru.clevertec.userservice.util.initdata.InitData.EMAIL_JOURNALIST;
import static ru.clevertec.userservice.util.initdata.InitData.ID_ADMIN;
import static ru.clevertec.userservice.util.initdata.InitData.ID_JOURNALIST;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserIdentityService userIdentityService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserMapper userMapper;

    @Nested
    class RegisterJournalist {

        @Test
        void shouldCheckSuccessRegistration() {
            // given
            UserRegisterRequest registerRequest = UserTestData.getRegisterRequestJournalist();
            User savedUser = UserTestData.getJournalist();
            when(userMapper.fromRequest(registerRequest, Status.ACTIVE, ROLE_JOURNALIST))
                    .thenReturn(savedUser);
            when(userRepository.saveAndFlush(savedUser))
                    .thenReturn(savedUser);

            // when
            userService.registerJournalist(registerRequest);

            // then
            verify(userRepository, times(1)).saveAndFlush(any(User.class));
        }
    }

    @Nested
    class RegisterSubscriber {

        @Test
        void shouldCheckSuccessRegistration() {
            // given
            UserRegisterRequest registerRequest = UserTestData.getRegisterRequestSubscriber();
            User savedUser = UserTestData.getSubscriber();
            when(userMapper.fromRequest(registerRequest, Status.ACTIVE, ROLE_SUBSCRIBER))
                    .thenReturn(savedUser);
            when(userRepository.saveAndFlush(any(User.class)))
                    .thenReturn(savedUser);

            // when
            userService.registerSubscriber(registerRequest);

            // then
            verify(userRepository, times(1)).saveAndFlush(any(User.class));
        }
    }

    @Nested
    class GetAllUsers {

        @Test
        void shouldReturnListOfPersonResponse() {
            // given
            int expectedSize = 1;
            List<User> usersList = List.of(UserTestData.getJournalist());
            Page<User> page = new PageImpl<>(usersList);
            when(userRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<UserResponse> actual = userService.getAllUsers(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldCheckEmpty() {
            // given
            Page<User> page = new PageImpl<>(List.of());
            when(userRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<UserResponse> actual = userService.getAllUsers(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindUserById {

        @Test
        void shouldReturnExpectedUserResponse() {
            // given
            UUID userId = ID_JOURNALIST;
            User user = UserTestData.getJournalist();
            UserResponse expectedResponse = UserTestData.getUserResponse();
            when(userIdentityService.getUserOrThrowException(userId))
                    .thenReturn(user);
            when(userMapper.toResponse(user))
                    .thenReturn(expectedResponse);

            // when
            UserResponse actualResponse = userService.findUserById(userId);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }

    @Nested
    class FindByUserEmail {

        @Test
        void shouldReturnExpectedUser() {
            // given
            String userEmail = EMAIL_JOURNALIST;
            User expectedUser = UserTestData.getJournalist();
            when(userRepository.findByEmailAndStatus(userEmail, Status.ACTIVE))
                    .thenReturn(Optional.of(expectedUser));

            // when
            User actualUser = userService.findActiveUserByEmailOrThrowException(userEmail);

            // then
            assertThat(actualUser).isEqualTo(expectedUser);
        }

        @Test
        void shouldReturnThrowExceptionWhenUserNotFound() {
            // given
            String userEmail = EMAIL_JOURNALIST;

            // when, then
            assertThatThrownBy(() -> userService.findActiveUserByEmailOrThrowException(userEmail))
                    .isInstanceOf(NoSuchUserEmailException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_EXIST.getMessage() + userEmail);
        }
    }

    @Nested
    class Update {

        @Test
        void shouldReturnExpectedUserResponse() {
            // given
            UUID userId = ID_JOURNALIST;
            UserUpdateRequest updateRequest = UserTestData.getUserUpdateRequest();
            User userInDB = UserTestData.getJournalist();
            User updatedUser = UserTestData.getJournalist();
            UserResponse expectedResponse = UserTestData.getUserResponse();
            when(userIdentityService.getUserOrThrowException(userId))
                    .thenReturn(userInDB);
            doNothing().when(userMapper).update(userInDB, updateRequest, userId);
            when(userRepository.save(userInDB))
                    .thenReturn(updatedUser);
            when(userMapper.toResponse(userInDB))
                    .thenReturn(expectedResponse);

            // when
            UserResponse actualResponse = userService.updateUserById(userId, updateRequest);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }

    @Nested
    class DeactivateUser {

        @Test
        void shouldDeactivateUser() {
            // given
            UUID userId = ID_JOURNALIST;
            User user = UserTestData.getJournalist();
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            userService.deactivateUserById(userId);

            // then
            verify(userRepository, times(1)).save(user);
            assertThat(user.getStatus()).isEqualTo(Status.NOT_ACTIVE);
        }

        @Test
        void shouldThrowExceptionWhenUserIsAdmin() {
            // given
            UUID userId = ID_ADMIN;
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            User user = UserTestData.getAdmin();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));

            // when, then
            assertThatThrownBy(() -> userService.deactivateUserById(userId))
                    .isInstanceOf(InformationChangeStatusUserException.class);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFound() {
            // given
            UUID userId = ID_ADMIN;
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> userService.deactivateUserById(userId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.USER_NOT_FOUND.getMessage() + userId);
        }
    }

    @Nested
    class DeleteUser {

        @Test
        void shouldDeletedUser() {
            // given
            UUID userId = ID_JOURNALIST;
            User user = UserTestData.getJournalist();
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            // when
            userService.deleteUserById(userId);

            // then
            verify(userRepository, times(1)).save(user);
            assertThat(user.getStatus()).isEqualTo(Status.DELETED);
        }
    }
}