package ru.clevertec.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record Filter(

        @NotBlank
        @Size(min = 1, max = 50)
        String part) {
}