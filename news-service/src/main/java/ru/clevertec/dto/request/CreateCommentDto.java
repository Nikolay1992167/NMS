package ru.clevertec.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateCommentDto(

        @NotBlank
        @Size(min = 3, max = 500)
        String text,

        @NotNull
        UUID newsId) {
}