package ru.clevertec.userservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.userservice.enums.error_response.ErrorMessage;
import ru.clevertec.userservice.security.jwt.JwtUser;

import java.util.UUID;


@UtilityClass
public class AuthUtil {

    public static UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUser jwtUser) {
            return jwtUser.getId();
        }
        throw new IllegalStateException(ErrorMessage.ERROR_EXTRACTION.getMessage());
    }
}