package ru.clevertec.dto.response;

import ru.clevertec.enams.Status;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        UUID createdBy,
        UUID updatedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String firstName,
        String lastName,
        String password,
        String email,
        List<RoleResponse> roles,
        Status status) implements Serializable {
}