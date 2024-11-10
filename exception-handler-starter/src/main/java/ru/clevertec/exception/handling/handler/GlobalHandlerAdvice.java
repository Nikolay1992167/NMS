package ru.clevertec.exception.handling.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.clevertec.exception.handling.exception.AccessException;
import ru.clevertec.exception.handling.exception.CreateObjectException;
import ru.clevertec.exception.handling.exception.CustomServerException;
import ru.clevertec.exception.handling.exception.InformationChangeStatusUserException;
import ru.clevertec.exception.handling.exception.JwtParsingException;
import ru.clevertec.exception.handling.exception.NoSuchUserEmailException;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.exception.handling.exception.ParsingException;
import ru.clevertec.exception.handling.exception.UniqueEmailException;
import ru.clevertec.exception.handling.model.IncorrectData;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerAdvice {

    @ExceptionHandler(ParsingException.class)
    public ResponseEntity<IncorrectData> parsingException(ParsingException exception) {
        return getResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomServerException.class)
    public ResponseEntity<IncorrectData> customServerException(CustomServerException exception) {
        return getResponse(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<IncorrectData> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return getResponse("Incorrectly entered newsId", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IncorrectData> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return getResponse(errors.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreateObjectException.class)
    public ResponseEntity<IncorrectData> createObjectException(CreateObjectException exception) {
        return getResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<IncorrectData> accessDeniedException(AccessException exception) {
        return getResponse(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<IncorrectData> illegalStateException(IllegalStateException exception) {
        return getResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<IncorrectData> accessDeniedException(AccessDeniedException exception) {
        return getResponse(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<IncorrectData> webClientRequestException(IllegalArgumentException exception) {
        return getResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<IncorrectData> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return getResponse("UUID was entered incorrectly!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InformationChangeStatusUserException.class)
    public ResponseEntity<IncorrectData> informationChangeStatusUserException(InformationChangeStatusUserException exception) {
        return getResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtParsingException.class)
    public ResponseEntity<IncorrectData> jwtParsingException(JwtParsingException exception) {
        return getResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<IncorrectData> usernameNotFoundException(UsernameNotFoundException exception) {
        return getResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchUserEmailException.class)
    public ResponseEntity<IncorrectData> noSuchUserEmailException(NoSuchUserEmailException exception) {
        return getResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<IncorrectData> notFoundException(NotFoundException exception) {
        return getResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueEmailException.class)
    public ResponseEntity<IncorrectData> uniqueEmailException(UniqueEmailException exception) {
        return getResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<IncorrectData> badRequest(HttpClientErrorException.BadRequest exception) {
        return getResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<IncorrectData> getResponse(String message, HttpStatus status) {
        IncorrectData incorrectData = new IncorrectData(LocalDateTime.now(), message, status.value());
        return ResponseEntity.status(status).body(incorrectData);
    }
}