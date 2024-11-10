package ru.clevertec.userservice.mapper.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.security.jwt.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class JwtTokenMapper {
    private final JwtTokenProvider jwtTokenProvider;

    @Named("createAccessToken")
    public String createAccessToken(User user) {
        return jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRoles());
    }

    @Named("createRefreshToken")
    public String createRefreshToken(User user) {
        return jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());
    }
}