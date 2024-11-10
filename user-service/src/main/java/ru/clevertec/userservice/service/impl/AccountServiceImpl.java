package ru.clevertec.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.userservice.dto.response.UserResponse;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.service.AccountService;
import ru.clevertec.userservice.service.UserIdentityService;
import ru.clevertec.userservice.util.AuthUtil;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserMapper userMapper;

    private final UserIdentityService userIdentityService;

    @Override
    public UserResponse getUserData() {
        User user = userIdentityService.getUserOrThrowException(AuthUtil.getUserId());
        return userMapper.toResponse(user);
    }
}