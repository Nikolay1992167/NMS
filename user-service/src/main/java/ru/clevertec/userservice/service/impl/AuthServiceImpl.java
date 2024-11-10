package ru.clevertec.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.clevertec.userservice.dto.request.JwtRequest;
import ru.clevertec.userservice.dto.response.JwtResponse;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.security.jwt.JwtTokenProvider;
import ru.clevertec.userservice.service.AuthService;
import ru.clevertec.userservice.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));

        User userInDB = userService.findActiveUserByEmailOrThrowException(loginRequest.getEmail());

        return userMapper.toJwtResponse(userInDB);
    }

    @Override
    public JwtResponse getRefreshToken(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }
}