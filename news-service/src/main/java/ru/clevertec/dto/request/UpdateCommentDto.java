package ru.clevertec.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateCommentDto(

        @NotBlank
        String text) {
}