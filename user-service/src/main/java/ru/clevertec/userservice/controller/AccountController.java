package ru.clevertec.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.logging.annotation.Logging;
import ru.clevertec.userservice.controller.openapi.AccountOpenApi;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.service.AccountService;

@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class AccountController implements AccountOpenApi {
    private final AccountService accountService;

    @Override
    @GetMapping("/details")
    public UserResponse getUserData() {
        return accountService.getUserData();
    }
}