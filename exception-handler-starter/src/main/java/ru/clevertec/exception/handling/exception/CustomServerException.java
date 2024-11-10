package ru.clevertec.exception.handling.exception;

public class CustomServerException extends RuntimeException {

    public CustomServerException(String message) {
        super(message);
    }
}