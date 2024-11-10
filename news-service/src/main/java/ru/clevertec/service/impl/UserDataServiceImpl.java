package ru.clevertec.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.dto.response.UserResponse;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.exception.handling.exception.CustomServerException;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.exception.handling.exception.ParsingException;
import ru.clevertec.exception.handling.model.IncorrectData;
import ru.clevertec.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {
    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    @Override
    public UserResponse getUserData(String authorizationToken) {
        return restClient.get()
                .uri("/api/v1/users/details")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, authorizationToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(UserResponse.class);
    }

    @Override
    public UserResponse getUserData(UUID userId, String authorizationToken) {
        try {
            return restClient.get()
                    .uri("/api/v1/admin/" + userId)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, authorizationToken)
                    .retrieve()
                    .body(UserResponse.class);
        } catch (HttpClientErrorException exception) {
            IncorrectData incorrectData;
            try {
                incorrectData = objectMapper.readValue(exception.getResponseBodyAsString(), IncorrectData.class);
            } catch (JsonProcessingException e) {
                throw new ParsingException(ErrorMessage.ERROR_PARSING_RESPONSE_TO_ERROR.getMessage());
            }
            throw new NotFoundException(incorrectData.errorMessage());
        } catch (HttpServerErrorException exception) {
            throw new CustomServerException(exception.getMessage());
        }
    }
}