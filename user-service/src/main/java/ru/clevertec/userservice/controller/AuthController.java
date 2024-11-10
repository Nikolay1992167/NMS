package ru.clevertec.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.logging.annotation.Logging;
import ru.clevertec.userservice.controller.openapi.AuthOpenApi;
import ru.clevertec.userservice.dto.request.JwtRequest;
import ru.clevertec.userservice.dto.request.RefreshTokenRequest;
import ru.clevertec.userservice.dto.request.UserRegisterRequest;
import ru.clevertec.userservice.dto.response.JwtResponse;
import ru.clevertec.userservice.service.AuthService;
import ru.clevertec.userservice.service.UserService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Logging
@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController implements AuthOpenApi {
    private final AuthService authService;
    private final UserService userService;

    @Override
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register/journalist")
    public void registerJournalist(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {
        userService.registerJournalist(userRegisterRequest);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register/subscriber")
    public void registerSubscriber(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {
        userService.registerSubscriber(userRegisterRequest);
    }

    @Override
    @PostMapping("/refresh")
    public JwtResponse getRefreshToken(@Validated @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.getRefreshToken(refreshTokenRequest.getRefreshToken());
    }
}