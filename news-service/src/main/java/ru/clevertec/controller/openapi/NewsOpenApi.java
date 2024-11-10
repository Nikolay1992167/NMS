package ru.clevertec.controller.openapi;

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
import ru.clevertec.dto.request.CreateNewsDto;
import ru.clevertec.dto.request.CreateNewsDtoJournalist;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.request.UpdateCommentDto;
import ru.clevertec.dto.response.ResponseCommentNews;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.exception.handling.model.IncorrectData;

import java.util.UUID;

@Tag(name = "News", description = "The News Api")
public interface NewsOpenApi {

    @Operation(
            method = "GET",
            tags = "News",
            description = "Get news by uuid",
            parameters = {
                    @Parameter(name = "uuid", description = "Id of News", example = "6a268b16-85ea-491f-be9f-f2e28d1a4895")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseCommentNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                "newsId": "5a0d7e7c-bbac-4165-a4bd-cc9ca0b05ef7",
                                                "createdBy": null,
                                                "updatedBy": null,
                                                "createdAt": "2024-04-18T14:15:05.326235",
                                                "updatedAt": "2024-04-18T14:15:05.326235",
                                                "text": "It is a good car.",
                                                "username": "Yury",
                                                "newsId": "389ec033-0631-4c0a-825b-e9ed165104d7"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "400", description = "Id is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-18T22:33:33.933514",
                                        "error_message": "UUID was entered incorrectly!",
                                        "error_status": 400
                                    }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "News not found.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                                "timestamp": "2024-04-23T22:26:35.4767938",
                                "error_message": "News not found with 6a268b16-85ea-491f-be9f-f2e28d1a4896",
                                "error_status": 404
                            }
                            """)))
            }
    )
    ResponseNews findNewsById(UUID newsId);

    @Operation(
            method = "GET",
            tags = "News",
            description = "Get page of news",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseCommentNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                      "content": [
                                                          {
                                                              "id": "6a268b16-85ea-491f-be9f-f2e28d1a4895",
                                                              "createdBy": null,
                                                              "updatedBy": null,
                                                              "createdAt": "2024-04-18T14:15:05.319254",
                                                              "updatedAt": "2024-04-18T14:15:05.319254",
                                                              "title": "SpaceX провела третий испытательный полёт Starship",
                                                              "text": "14 марта 2024 года SpaceX запустила в третий испытательный полёт корабль Starship Super Heavy. Корабль стартовал с площадки OLP-1 Starbase в Техасе. Цель миссии Integrated Flight Test 3 — выход корабля на расчётную траекторию орбиты.",
                                                              "idAuthor": "2512c298-6a1d-48d7-a12d-b51069aceb08"
                                                          },
                                                          {
                                                              "id": "389ec033-0631-4c0a-825b-e9ed165104d7",
                                                              "createdBy": null,
                                                              "updatedBy": null,
                                                              "createdAt": "2024-04-18T14:15:05.319254",
                                                              "updatedAt": "2024-04-18T14:15:05.319254",
                                                              "title": "Chinese company Xiaomi releases first electric vehicle",
                                                              "text": "Xiaomi is pushing into the overseas market by selling lower-priced models of their new electric vehicle. The US and EU have launched investigations into the Chinese EV industry.",
                                                              "idAuthor": "2512c298-6a1d-48d7-a12d-b51069aceb08"
                                                          },
                                                          {
                                                              "id": "0a30edeb-a294-43bc-a8b8-e5de7e2c8e08",
                                                              "createdBy": null,
                                                              "updatedBy": null,
                                                              "createdAt": "2024-04-18T14:15:05.319254",
                                                              "updatedAt": "2024-04-18T14:15:05.319254",
                                                              "title": "The Mercedes-Benz E450 Has One Mission: Comfort Above Everything",
                                                              "text": "You know, there’s still something to a good sedan. It’s becoming a lost craft, with all the rigamarole surrounding crossovers, SUVs, and trucks. Or even the super-ification of every sedan left on the market. The joy of a luxury sedan—emphasis on luxury—has almost been lost.",
                                                              "idAuthor": "2512c298-6a1d-48d7-a12d-b51069aceb08"
                                                          },
                                                          {
                                                              "id": "f0759f37-00ec-456f-a555-e634dc90a1f6",
                                                              "createdBy": null,
                                                              "updatedBy": null,
                                                              "createdAt": "2024-04-18T14:15:05.319254",
                                                              "updatedAt": "2024-04-18T14:15:05.319254",
                                                              "title": "The New Toyota 4Runner Shows Its Back End for the First Time",
                                                              "text": "The new Toyota 4Runner is coming soon. Today, we get our first official look at the forthcoming SUV. Toyota shared a teaser image on its Instagram account that shows the off-roaders back end with the 4Runner badge front and center.",
                                                              "idAuthor": "2512c298-6a1d-48d7-a12d-b51069aceb08"
                                                          },
                                                          {
                                                              "id": "d215d55e-fe9b-4946-b55f-6b428cf7a68b",
                                                              "createdBy": null,
                                                              "updatedBy": null,
                                                              "createdAt": "2024-04-18T14:15:05.319254",
                                                              "updatedAt": "2024-04-18T14:15:05.319254",
                                                              "title": "Audi Is Getting Rid of Its Confusing Naming Scheme",
                                                              "text": "Audi confused us all in 2017 when it introduced a double-digit naming scheme across the lineup. The terminology is based on output, from \\"30\\" for cars that have 109-128 horsepower up to \\"70\\" for vehicles with at least 536 hp. The two-numeral combination appears alongside the usual badges such as TFSI, TDI, and Quattro. After only seven years, the company has decided to move away from these vague designations.",
                                                              "idAuthor": "2512c298-6a1d-48d7-a12d-b51069aceb08"
                                                          }
                                                      ],
                                                      "pageable": {
                                                          "pageNumber": 0,
                                                          "pageSize": 20,
                                                          "sort": {
                                                              "empty": true,
                                                              "sorted": false,
                                                              "unsorted": true
                                                          },
                                                          "offset": 0,
                                                          "unpaged": false,
                                                          "paged": true
                                                      },
                                                      "last": true,
                                                      "totalPages": 1,
                                                      "totalElements": 5,
                                                      "first": true,
                                                      "size": 20,
                                                      "number": 0,
                                                      "sort": {
                                                          "empty": true,
                                                          "sorted": false,
                                                          "unsorted": true
                                                      },
                                                      "numberOfElements": 5,
                                                      "empty": false
                                                  }
                                            """)))
            }
    )
    Page<ResponseNews> getAllNews(Pageable pageable);

    @Operation(
            method = "GET",
            tags = "News",
            description = "Get page of news by filter",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Filter.class),
                            examples = @ExampleObject("""
                                    {
                                        "part": "запустила"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                        "content": [
                                                            {
                                                                "id": "6a268b16-85ea-491f-be9f-f2e28d1a4895",
                                                                "createdBy": null,
                                                                "updatedBy": null,
                                                                "createdAt": "2024-04-18T14:15:05.319254",
                                                                "updatedAt": "2024-04-18T14:15:05.319254",
                                                                "title": "SpaceX провела третий испытательный полёт Starship",
                                                                "text": "14 марта 2024 года SpaceX запустила в третий испытательный полёт корабль Starship Super Heavy. Корабль стартовал с площадки OLP-1 Starbase в Техасе. Цель миссии Integrated Flight Test 3 — выход корабля на расчётную траекторию орбиты.",
                                                                "idAuthor": "2512c298-6a1d-48d7-a12d-b51069aceb08"
                                                            }
                                                        ],
                                                        "pageable": {
                                                            "pageNumber": 0,
                                                            "pageSize": 20,
                                                            "sort": {
                                                                "empty": true,
                                                                "sorted": false,
                                                                "unsorted": true
                                                            },
                                                            "offset": 0,
                                                            "unpaged": false,
                                                            "paged": true
                                                        },
                                                        "last": true,
                                                        "totalPages": 1,
                                                        "totalElements": 1,
                                                        "first": true,
                                                        "size": 20,
                                                        "number": 0,
                                                        "sort": {
                                                            "empty": true,
                                                            "sorted": false,
                                                            "unsorted": true
                                                        },
                                                        "numberOfElements": 1,
                                                        "empty": false
                                                    }
                                            """)))
            }
    )
    Page<ResponseNews> findNewsByFilter(Filter filter, Pageable pageable);

    @Operation(
            method = "POST",
            tags = "News",
            description = "Create a news for user with role 'ADMIN'",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateNewsDto.class),
                            examples = @ExampleObject("""
                                    {
                                        "title":"First news",
                                        "text":"Example text!",
                                        "idAuthor":"b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "News created.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "4389c2d6-8c16-4234-a8ea-e5317d3a91f5",
                                                "createdBy": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                                "updatedBy": null,
                                                "createdAt": "2024-04-20T22:42:44.757449",
                                                "updatedAt": "2024-04-20T22:42:44.757449",
                                                "title": "First news",
                                                "text": "Example text!",
                                                "idAuthor": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"
                                            }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-20T22:51:55.9196106",
                                                "error_message": "Full authentication is required to access this resource",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "404", description = "Journalist not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-23T22:30:12.2565458",
                                                "error_message": "User not found with b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Request data is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-20T22:49:56.0696022",
                                                "error_message": "{title=размер должен находиться в диапазоне от 5 до 100}",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token without access.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:23:23.4668874",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    ResponseNews createNewsAdmin(CreateNewsDto createNewsDto, String token);

    @Operation(
            method = "POST",
            tags = "News",
            description = "Create a news for user with role 'JOURNALIST'",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateNewsDto.class),
                            examples = @ExampleObject("""
                                    {
                                        "title": "Great weather",
                                        "text": "The weather is determined by an eastern cyclone with abundant rainfall."
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "News created.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseNews.class),
                                    examples = @ExampleObject("""
                                    {
                                          "id": "376a7953-4b48-453b-9e05-06aab3d897e2",
                                          "createdBy": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                          "updatedBy": null,
                                          "createdAt": "2024-04-24T08:22:50.303188",
                                          "updatedAt": "2024-04-24T08:22:50.303188",
                                          "title": "Great weather",
                                          "text": "The weather is determined by an eastern cyclone with abundant rainfall.",
                                          "idAuthor": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"
                                    }
                                            """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-20T22:51:55.9196106",
                                                "error_message": "Full authentication is required to access this resource",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Request data is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-20T22:49:56.0696022",
                                                "error_message": "{title=размер должен находиться в диапазоне от 5 до 100}",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token without access.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-24T08:29:29.5165644",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    ResponseNews createNewsJournalist(CreateNewsDtoJournalist createNewsDtoJournalist);

    @Operation(
            method = "PUT",
            tags = "News",
            description = "Update a news by newsId for user with role 'ADMIN'",
            parameters = {
                    @Parameter(name = "id", description = "newsId of news", example = "4389c2d6-8c16-4234-a8ea-e5317d3a91f5")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCommentDto.class),
                            examples = @ExampleObject("""
                                    {
                                        "title": "Good news",
                                        "text": "This very Good news",
                                        "idAuthor": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                "newsId": "4389c2d6-8c16-4234-a8ea-e5317d3a91f5",
                                                "createdBy": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                                "updatedBy": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "createdAt": "2024-04-20T22:42:44.757449",
                                                "updatedAt": "2024-04-20T23:20:06.541507",
                                                "title": "Good news",
                                                "text": "This very Good news",
                                                "idAuthor": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404", description = "News not found.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:08:19.3778544",
                                                "error_message": "News not found with 9725df2c-b725-4572-98e5-aa60d430c758",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-18T16:11:40.4996957",
                                        "error_message": "UUID was entered incorrectly!",
                                        "error_status": 400
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:13:17.6538192",
                                                "error_message": "Full authentication is required to access this resource",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token without access.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:23:23.4668874",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    ResponseNews updateNewsAdmin(UUID newsId, CreateNewsDto createNewsDto, String token);

    @Operation(
            method = "PUT",
            tags = "News",
            description = "Update a news by newsId for user with role 'JOURNALIST'",
            parameters = {
                    @Parameter(name = "id", description = "newsId of news", example = "4389c2d6-8c16-4234-a8ea-e5317d3a91f5")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCommentDto.class),
                            examples = @ExampleObject("""
                                    {
                                        "title": "Updated news from journalist",
                                        "text": "The weather is great today."
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                "newsId": "376a7953-4b48-453b-9e05-06aab3d897e2",
                                                "createdBy": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                                "updatedBy": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12",
                                                "createdAt": "2024-04-24T08:22:50.303188",
                                                "updatedAt": "2024-04-24T08:36:46.080303",
                                                "title": "Updated news from journalist",
                                                "text": "The weather is great today.",
                                                "idAuthor": "b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404", description = "News not found.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-24T08:38:13.4409905",
                                                "error_message": "News not found with 376a7953-4b48-453b-9e05-06aab3d897e4",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-24T08:38:38.4295148",
                                        "error_message": "UUID was entered incorrectly!",
                                        "error_status": 400
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-24T08:39:01.0995678",
                                                "error_message": "Full authentication is required to access this resource",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token without access.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:23:23.4668874",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    ResponseNews updateNewsJournalist(UUID newsId, CreateNewsDtoJournalist createNewsDto);

    @Operation(
            method = "DELETE",
            tags = "News",
            description = "Delete a news by newsId",
            parameters = {
                    @Parameter(name = "id", description = "newsId of news", example = "9725df2c-b725-4572-98e5-aa60d430c757")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "News deleted."),
                    @ApiResponse(responseCode = "404", description = "News not found.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-21T18:18:11.8869399",
                                                "error_message": "News not found with d215d55e-fe9b-4946-b55f-6b428cf7a688",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-21T18:17:50.8301706",
                                        "error_message": "UUID was entered incorrectly!",
                                        "error_status": 400
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-21T18:15:43.6610815",
                                                "error_message": "Full authentication is required to access this resource",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token without access.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-21T18:15:05.9415192",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "User can't delete comment other user.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-21T18:17:29.405214",
                                                "error_message": "You have no right to change the data of other users!",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    void deleteNewsById(UUID newsId);
}