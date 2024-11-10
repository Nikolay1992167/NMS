package ru.clevertec.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ResponseCommentNews(UUID id,
                                  UUID createdBy,
                                  UUID updatedBy,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  String text,
                                  String username,
                                  UUID newsId) implements Serializable {
}