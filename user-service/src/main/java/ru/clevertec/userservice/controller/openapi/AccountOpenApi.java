package ru.clevertec.userservice.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.clevertec.userservice.dto.response.UserResponse;

@Tag(name = "User", description = "The User Api")
public interface AccountOpenApi {

    @Operation(
            method = "POST",
            tags = "Account",
            description = "Get data about the user.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject("""
                                    {
                                        "token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuMUBnb29nbGUuY29tIiwiaWQiOiJhMGVlYmM5OS05YzBiLTRlZjgtYmI2ZC02YmI5YmQzODBhMTEiLCJyb2xlcyI6WyJBRE1JTiJdLCJleHAiOjE3MzA5NjgxMTV9.OlQSKmPVrevcNoLyBba9BiiehCemA_L2q3UE7CSAFcSQeEGzdWqzcQwRNx22qbxO4oMJQakHI25bhBqZ3doAMw"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "createdBy": null,
                                                "updatedBy": null,
                                                "createdAt": "2024-11-07T07:22:39.896363",
                                                "updatedAt": "2024-11-07T07:22:39.896363",
                                                "firstName": "Ivan",
                                                "lastName": "Sidorov",
                                                "password": "$2a$10$bHjpNHpZh80bmtREfBcAouJkAfXnAxPxmjr2odWGqqEXWI6gA8xQ6",
                                                "email": "ivan1@google.com",
                                                "roles": [
                                                    {
                                                        "id": "73c65923-b5b1-42df-bd99-299180f287e0",
                                                        "name": "ADMIN"
                                                    }
                                                ],
                                                "status": "ACTIVE"
                                            }
                                            """)))
            }
    )
    UserResponse getUserData();
}