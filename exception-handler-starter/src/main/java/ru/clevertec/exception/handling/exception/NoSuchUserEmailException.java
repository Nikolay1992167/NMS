package ru.clevertec.exception.handling.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class NoSuchUserEmailException extends InternalAuthenticationServiceException {

    public NoSuchUserEmailException(String message) {
        super(message);
    }
}