package ru.clevertec.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.service.UserIdentityService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserIdentityServiceImpl implements UserIdentityService {
    private final UserRepository userRepository;

    @Override
    public User getUserOrThrowException(UUID userId) {
        return userRepository.findUserFetchRolesById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND.getMessage() + userId));
    }
}