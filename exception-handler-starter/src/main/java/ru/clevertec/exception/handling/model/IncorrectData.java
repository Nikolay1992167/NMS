package ru.clevertec.exception.handling.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record IncorrectData(LocalDateTime timestamp,
                            String errorMessage,
                            int errorStatus) {
}