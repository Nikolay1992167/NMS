package ru.clevertec.exception.handling.exception;

public class UniqueEmailException extends RuntimeException{

    public UniqueEmailException(String message) {
        super(message);
    }
}