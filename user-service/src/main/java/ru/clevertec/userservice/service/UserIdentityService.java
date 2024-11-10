package ru.clevertec.userservice.service;

import ru.clevertec.userservice.model.User;

import java.util.UUID;

public interface UserIdentityService {

    User getUserOrThrowException(UUID userId);
}