package ru.clevertec.userservice.service;

import ru.clevertec.userservice.dto.request.JwtRequest;
import ru.clevertec.userservice.dto.response.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse getRefreshToken(String refreshToken);
}