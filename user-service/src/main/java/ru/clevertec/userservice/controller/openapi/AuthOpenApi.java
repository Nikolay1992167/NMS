package ru.clevertec.userservice.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.clevertec.exception.handling.model.IncorrectData;
import ru.clevertec.userservice.dto.request.JwtRequest;
import ru.clevertec.userservice.dto.request.RefreshTokenRequest;
import ru.clevertec.userservice.dto.request.UserRegisterRequest;
import ru.clevertec.userservice.dto.response.JwtResponse;

@Tag(name = "Authentication", description = "The Authentication Api")
public interface AuthOpenApi {

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Authentication in app.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "email": "ivan@google.com",
                                        "password": "123456789"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "email": "ivan@google.com",
                                                "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcwOTQ4MTQ5NH0.iTsgestQ0Udfll9RmVwHVRam-MLqrZ9qWWaruJsad7uAzs-Id9DFONSA3bGLoQXRQE5BzLeKnsNB5T6PIqYGWg",
                                                "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMjA2OTg5NH0.Bype1oc2qs72GVRqCP3cuZ__MmT0kZvjZog6djxD-UO0eCPe-wdY2Pak-_SmvH3tDM6o8jqb9uMGuJ07M7gA0Q"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "400", description = "Email is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T17:58:39.1950805",
                                                "error_message": "{email=должно иметь формат адреса электронной почты}",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "User with this email does not exist.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T18:00:37.0128514",
                                                "error_message": "User is not exist with grom@google.com",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "User with status not active.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T18:04:10.6152098",
                                                "error_message": "User is not active!",
                                                "error_status": 401
                                            }
                                    """)))
            }
    )
    JwtResponse authenticate(@Parameter(hidden = true) JwtRequest loginRequest);

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Registration journalist",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRegisterRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "firstName": "Pasha",
                                        "lastName": "Grom",
                                        "email": "grom@grom.com",
                                        "password": "63524178"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "The journalist was registered."),
                    @ApiResponse(responseCode = "400", description = "Email is exist in DB.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T18:06:30.1734508",
                                                "error_message": "Email is occupied! Another user is already registered by this gromov@google.com",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Request arguments are incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T18:10:22.8635364",
                                                "error_message": "{firstName=размер должен находиться в диапазоне от 2 до 40, email=должно иметь формат адреса электронной почты, lastName=размер должен находиться в диапазоне от 2 до 50}",
                                                "error_status": 400
                                            }
                                    """)))
            }
    )
    void registerJournalist(UserRegisterRequest request);

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Registration subscriber",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRegisterRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "firstName": "Olga",
                                        "lastName": "Popova",
                                        "email": "popova@email.com",
                                        "password": "9652314"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "The subscriber was registered."),
                    @ApiResponse(responseCode = "406", description = "Email is exist in DB.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T18:11:58.3443861",
                                                "error_message": "Email is occupied! Another user is already registered by this popova@email.com",
                                                "error_status": 406
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Request arguments are incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-03-03T18:13:14.5884612",
                                                "error_message": "{firstName=размер должен находиться в диапазоне от 2 до 40, lastName=размер должен находиться в диапазоне от 2 до 50, email=должно иметь формат адреса электронной почты}",
                                                "error_status": 400
                                            }
                                    """)))
            }
    )
    void registerSubscriber(UserRegisterRequest request);

    @Operation(
            method = "POST",
            tags = "Authentication",
            description = "Updated token",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject("""
                                    {
                                        "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcwOTA3ODQ2Nn0.6iwpfXY2eZDZCmxLrcXL1rdEs1ZYoKWTFGHl4spzabAT5hRuZ1pBZDHueydtxj-UeW_r4fsMDB386JUsKulsGQ"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                                "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcwOTA3ODQ4Nn0.dAagsoFXl3GyZUeHn-BUWtSakIkLv5JEJNDycuwSW_iNfW04f5eroXVJtWRX8yCC6gbgzqcPi-BupOyTu8dXdQ",
                                                "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuQGdvb2dsZS5jb20iLCJpZCI6ImEwZWViYzk5LTljMGItNGVmOC1iYjZkLTZiYjliZDM4MGExMSIsImV4cCI6MTcxMTY2Njg4Nn0.W5FbSle_jHpATJ6SOrWflCdueJSNk9IaT8v1SUSmXN-4HTmL-gmYi_xJGyW7LgCLRumMR4Ad0TuRHldI_p1-uQ"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "401", description = "Token is expired.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-03-04T01:33:11.6330552",
                                        "error_message": "Full authentication is required to access this resource",
                                        "error_status": 401
                                    }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Token is not valid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                   {
                                        "timestamp": "2024-03-04T01:55:32.2846064",
                                        "error_message": "{refreshToken=должно соответствовать \\"^[a-zA-Z0-9_-]+\\\\.[a-zA-Z0-9_-]+\\\\.[a-zA-Z0-9_-]+$\\"}",
                                        "error_status": 400
                                   }
                                    """)))
            }
    )
    JwtResponse getRefreshToken(RefreshTokenRequest refreshTokenRequest);
}