package ru.clevertec.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateNewsDto(

        @NotBlank
        @Size(min = 5, max = 100)
        String title,

        @NotBlank
        @Size(min = 5, max = 500)
        String text,

        @NotNull
        UUID idAuthor) {
}