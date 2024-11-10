package ru.clevertec.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.userservice.dto.request.UserRegisterRequest;
import ru.clevertec.userservice.dto.request.UserUpdateRequest;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.model.User;

import java.util.UUID;

public interface UserService {

    void registerJournalist(UserRegisterRequest request);

    void registerSubscriber(UserRegisterRequest request);

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse findUserById(UUID userId);

    User findActiveUserByEmailOrThrowException(String userEmail);

    UserResponse updateUserById(UUID userId, UserUpdateRequest userUpdateRequest);

    void deactivateUserById(UUID userId);

    void deleteUserById(UUID userId);
}