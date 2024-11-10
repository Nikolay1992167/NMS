package ru.clevertec.service;


import ru.clevertec.dto.response.UserResponse;

import java.util.UUID;

public interface UserDataService {

    /**
     * This method finds and returns user with his data by his authorizationToken.
     *
     * @param authorizationToken a string containing the authentication authorizationToken in the request header.
     * @return the {@link UserResponse} object containing the user's data.
     */
    UserResponse getUserData(String authorizationToken);

    /**
     * This method finds and returns user with his data by his id.
     *
     * @param userId id of user.
     * @return the {@link UserResponse} object containing the user's data.
     */
    UserResponse getUserData(UUID userId, String authorizationToken);
}