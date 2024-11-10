package ru.clevertec.userservice.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.exception.handling.model.IncorrectData;
import ru.clevertec.userservice.dto.request.UserUpdateRequest;
import ru.clevertec.userservice.dto.response.UserResponse;

import java.util.UUID;

@Tag(name = "Admin", description = "The Admin Api")
public interface AdminOpenApi {

    @Operation(
            method = "GET",
            tags = "Admin",
            description = "Get page of users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "content": [
                                                    {
                                                        "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                        "createdBy": null,
                                                        "updatedBy": null,
                                                        "createdAt": "2024-05-01T23:21:28.714835",
                                                        "updatedAt": "2024-05-01T23:21:28.714835",
                                                        "firstName": "Ivan",
                                                        "lastName": "Sidorov",
                                                        "password": "$2a$10$P9GsTtz4wB/6xDygXcJfse3g0MhDG2bXnLx9OeT5yX9k.Cdg7PjaO",
                                                        "email": "ivan@google.com",
                                                        "roles": [
                                                            {
                                                                "id": "73c65923-b5b1-42df-bd99-299180f287e0",
                                                                "name": "ADMIN"
                                                            }
                                                        ],
                                                        "status": "ACTIVE"
                                                    },
                                                    {
                                                        "id": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                                        "createdBy": null,
                                                        "updatedBy": null,
                                                        "createdAt": "2024-05-01T23:21:28.714835",
                                                        "updatedAt": "2024-05-01T23:21:28.714835",
                                                        "firstName": "Egor",
                                                        "lastName": "Strelin",
                                                        "password": "$2a$10$HQF7p0wcws4NDvhIUdOQDeDOFu3OokJseoz.2gvcFOTsSqQGOGG3K",
                                                        "email": "strelin@mail.ru",
                                                        "roles": [
                                                            {
                                                                "id": "2512c298-6a1d-48d7-a12d-b51069aceb08",
                                                                "name": "JOURNALIST"
                                                            }
                                                        ],
                                                        "status": "ACTIVE"
                                                    }
                                                ],
                                                "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 20,
                                                    "sort": {
                                                        "empty": true,
                                                        "unsorted": true,
                                                        "sorted": false
                                                    },
                                                    "offset": 0,
                                                    "unpaged": false,
                                                    "paged": true
                                                },
                                                "last": true,
                                                "totalPages": 1,
                                                "totalElements": 5,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                    "empty": true,
                                                    "unsorted": true,
                                                    "sorted": false
                                                },
                                                "first": true,
                                                "numberOfElements": 5,
                                                "empty": false
                                            }
                                            """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T20:15:31.7881304",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is expired.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:07:49.848746",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:07:49Z, a difference of 608823848 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is invalid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:08:13.1738829",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token with a non-admin role.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T20:38:59.6169887",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    Page<UserResponse> getAllUsers(Pageable pageable);

    @Operation(
            method = "GET",
            tags = "Admin",
            description = "Get a User by id",
            parameters = {
                    @Parameter(name = "userId", description = "Id of User", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                                "createdBy": null,
                                                "updatedBy": null,
                                                "createdAt": "2024-05-01T21:04:43.644024",
                                                "updatedAt": "2024-05-01T21:04:43.644024",
                                                "firstName": "Egor",
                                                "lastName": "Strelin",
                                                "password": "$2a$10$uuwsQHbWZMIUMTuxKwij8e/l5zea9.Q2XW0eG3Bs/2fUMarbqiymG",
                                                "email": "strelin@mail.ru",
                                                "roles": [
                                                    {
                                                        "id": "2512c298-6a1d-48d7-a12d-b51069aceb08",
                                                        "name": "JOURNALIST"
                                                    }
                                                ],
                                                "status": "ACTIVE"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T20:41:58.7865553",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is expired.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:08:47.8864875",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:08:47Z, a difference of 608881885 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is invalid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:09:04.1926719",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token with a non-admin role.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T20:45:26.1894423",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "Id is not existent.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:11:33.2154937",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15 not found!",
                                                "error_status": 404
                                            }
                                    """))),
            }
    )
    UserResponse findUserById(UUID userId);

    @Operation(
            method = "PUT",
            tags = "Admin",
            description = "Update an user by his id.",
            parameters = {
                    @Parameter(name = "userId", description = "Id of user", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "firstName": "Nikolay",
                                        "lastName": "Minich",
                                        "password": "8776868",
                                        "email": "nikolayv@example.com"
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
                                                "id": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
                                                "createdBy": null,
                                                "updatedBy": "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
                                                "createdAt": "2024-05-01T21:35:17.943424",
                                                "updatedAt": "2024-05-01T21:35:17.943424",
                                                "firstName": "Nikolay",
                                                "lastName": "Minich",
                                                "password": "$2a$10$WflfzbK.w8gn1nNJtodpE.EO/.2Qv.89i3YWHJUc1HFySq64ZImYS",
                                                "email": "nikolay555@example.com",
                                                "roles": [
                                                    {
                                                        "id": "f5b50fda-f157-4a8b-948c-6705206c81c6",
                                                        "name": "SUBSCRIBER"
                                                    }
                                                ],
                                                "status": "ACTIVE"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "400", description = "Id is invalid.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:04:33.3089845",
                                                "error_message": "UUID was entered incorrectly!",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T21:05:16.3651964",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is expired.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:10:03.6470458",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:10:03Z, a difference of 608957647 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is invalid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:10:25.6642296",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "Id is not existent.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:13:39.1105685",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-00000006bb15 not found!",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Request data is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:14:31.5823614",
                                                "error_message": "{lastName=размер должен находиться в диапазоне от 2 до 50, firstName=размер должен находиться в диапазоне от 2 до 40, email=должно иметь формат адреса электронной почты}",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Email is not unique.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:15:04.0445334",
                                                "error_message": "Email is occupied! Another user is already registered by this nikolayv@example.com",
                                                "error_status": 400
                                            }
                                    """)))
            }
    )
    UserResponse updateUserById(UUID userId, UserUpdateRequest userUpdateRequest);

    @Operation(
            method = "PATCH",
            tags = "Admin",
            description = "Changing the user's status to NOT_ACTIVE",
            parameters = {
                    @Parameter(name = "userId", description = "Id of user", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "The status has been changed to NOT_ACTIVE."),
                    @ApiResponse(responseCode = "404", description = "Id is not existent.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:16:40.4767385",
                                                "error_message": "User with c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18 not found!",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is invalid.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:17:09.5058146",
                                                "error_message": "UUID was entered incorrectly!",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Not change status of a user with the role ADMIN.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:17:42.2449021",
                                                "error_message": "You cannot change the status of a user with the ADMIN role.",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T21:18:03.3175993",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is expired.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:18:36.2612456",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:18:36Z, a difference of 609470261 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is invalid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:18:52.4644725",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """)))
            }
    )
    void deactivateUserById(UUID userId);

    @Operation(
            method = "DELETE",
            tags = "Admin",
            description = "Changing the user's status to DELETED",
            parameters = {
                    @Parameter(name = "userId", description = "Id of user", example = "c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "The status has been changed to DELETED."),
                    @ApiResponse(responseCode = "404", description = "Id is not existent.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:19:45.6832412",
                                                "error_message": "User with a0eebc99-9c0b-4ef8-bb6d-6bb9bd380154 not found!",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is invalid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:20:10.2958205",
                                                "error_message": "UUID was entered incorrectly!",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "409", description = "Not change status of a user with the role ADMIN.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:20:43.5178811",
                                                "error_message": "You cannot change the status of a user with the ADMIN role.",
                                                "error_status": 409
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-03T21:21:01.5391987",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is expired.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:21:30.5794483",
                                                "error_message": "JWT expired at 2024-02-25T17:00:46Z. Current time: 2024-03-03T18:21:30Z, a difference of 609644579 milliseconds.  Allowed clock skew: 0 milliseconds.",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is invalid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T21:21:45.4513425",
                                                "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
                                                "error_status": 401
                                            }
                                    """)))
            }
    )
    void deleteUserById(UUID userId);
}