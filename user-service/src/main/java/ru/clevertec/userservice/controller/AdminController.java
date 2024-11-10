package ru.clevertec.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.logging.annotation.Logging;
import ru.clevertec.userservice.controller.openapi.AdminOpenApi;
import ru.clevertec.userservice.dto.request.UserUpdateRequest;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.service.UserService;

import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Logging
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping(value = "/api/v1/admin", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminController implements AdminOpenApi {
    private final UserService userService;

    @Override
    @GetMapping
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @Override
    @GetMapping("/{userId}")
    public UserResponse findUserById(@PathVariable UUID userId) {
        return userService.findUserById(userId);
    }

    @Override
    @PutMapping("/{userId}")
    public UserResponse updateUserById(@PathVariable UUID userId,
                                       @Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUserById(userId, userUpdateRequest);
    }

    @Override
    @PatchMapping("/deactivate/{userId}")
    public void deactivateUserById(@PathVariable UUID userId) {
        userService.deactivateUserById(userId);
    }

    @Override
    @DeleteMapping("/delete/{userId}")
    public void deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
    }
}