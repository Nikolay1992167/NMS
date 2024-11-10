package ru.clevertec.util;

import ru.clevertec.dto.request.JwtUser;
import ru.clevertec.enams.ErrorMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class AuthUtil {

    public static UUID getId() {
        return getJwtUser().getId();
    }

    public static JwtUser getUser() {
        return getJwtUser();
    }

    private static JwtUser getJwtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUser jwtUser) {
            return jwtUser;
        }
        throw new IllegalStateException(ErrorMessage.ERROR_EXTRACTION.getMessage());
    }
}