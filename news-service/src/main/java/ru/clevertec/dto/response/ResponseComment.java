package ru.clevertec.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ResponseComment(UUID id,
                              UUID createdBy,
                              UUID updatedBy,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              String text,
                              String username) implements Serializable {
}