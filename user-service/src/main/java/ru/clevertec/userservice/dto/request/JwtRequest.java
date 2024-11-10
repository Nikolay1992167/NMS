package ru.clevertec.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ru.clevertec.userservice.util.Constants.EMAIL_ERROR;
import static ru.clevertec.userservice.util.Constants.SIZE_PASSWORD_ERROR;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {

    @NotBlank
    @Email(message = EMAIL_ERROR)
    private String email;

    @NotBlank
    @Size(min = 3, max = 100, message = SIZE_PASSWORD_ERROR)
    private String password;
}