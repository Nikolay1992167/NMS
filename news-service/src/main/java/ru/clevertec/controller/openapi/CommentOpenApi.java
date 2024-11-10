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
import ru.clevertec.dto.request.CreateCommentDto;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.request.UpdateCommentDto;
import ru.clevertec.dto.response.ResponseCommentNews;
import ru.clevertec.dto.response.ResponseNewsWithComments;
import ru.clevertec.exception.handling.model.IncorrectData;

import java.util.UUID;

@Tag(name = "Comment", description = "The Comment Api")
public interface CommentOpenApi {

    @Operation(
            method = "GET",
            tags = "Comment",
            description = "Get comment by uuid",
            parameters = {
                    @Parameter(name = "commentId", description = "Id of Comment", example = "5a0d7e7c-bbac-4165-a4bd-cc9ca0b05ef7")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseCommentNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "5a0d7e7c-bbac-4165-a4bd-cc9ca0b05ef7",
                                                "createdBy": null,
                                                "updatedBy": null,
                                                "createdAt": "2024-04-18T14:15:05.326235",
                                                "updatedAt": "2024-04-18T14:15:05.326235",
                                                "text": "It is a good car.",
                                                "username": "Yury",
                                                "newsId": "389ec033-0631-4c0a-825b-e9ed165104d7"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404", description = "Comment not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-23T20:51:34.5814514",
                                        "error_message": "Comment not found with 5a0d7e7c-bbac-4165-a4bd-00cc9ca0b05e",
                                        "error_status": 404
                                    }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is invalid.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-23T22:18:25.2408902",
                                        "error_message": "UUID was entered incorrectly!",
                                        "error_status": 400
                                    }
                                    """)))
            }
    )
    ResponseCommentNews findCommentById(UUID commentId);

    @Operation(
            method = "GET",
            tags = "Comments by news id",
            description = "Get comment by news uuid",
            parameters = {
                    @Parameter(name = "newsId", description = "Id of news", example = "6a268b16-85ea-491f-be9f-f2e28d1a4895")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseNewsWithComments.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "6a268b16-85ea-491f-be9f-f2e28d1a4895",
                                                "createdBy": null,
                                                "updatedBy": null,
                                                "createdAt": "2024-04-18T14:15:05.319254",
                                                "updatedAt": "2024-04-18T14:15:05.319254",
                                                "title": "SpaceX провела третий испытательный полёт Starship",
                                                "text": "14 марта 2024 года SpaceX запустила в третий испытательный полёт корабль Starship Super Heavy. Корабль стартовал с площадки OLP-1 Starbase в Техасе. Цель миссии Integrated Flight Test 3 — выход корабля на расчётную траекторию орбиты.",
                                                "idAuthor": "2512c298-6a1d-48d7-a12d-b51069aceb08",
                                                "comments": [
                                                    {
                                                        "id": "09434a73-e4e5-463b-a772-f282adef33a9",
                                                        "createdBy": null,
                                                        "updatedBy": null,
                                                        "createdAt": "2024-04-18T14:15:05.326235",
                                                        "updatedAt": "2024-04-18T14:15:05.326235",
                                                        "text": "Best company in America.",
                                                        "username": "Nikolay"
                                                    },
                                                    {
                                                        "id": "0af362c7-999e-428d-91ec-703ee540c54a",
                                                        "createdBy": null,
                                                        "updatedBy": null,
                                                        "createdAt": "2024-04-18T14:15:05.326235",
                                                        "updatedAt": "2024-04-18T14:15:05.326235",
                                                        "text": "Good news.",
                                                        "username": "Igor"
                                                    },
                                                    {
                                                        "id": "4d0eb5fb-6c3b-4512-9c75-7a8ce5eac4e9",
                                                        "createdBy": null,
                                                        "updatedBy": null,
                                                        "createdAt": "2024-04-18T14:15:05.326235",
                                                        "updatedAt": "2024-04-18T14:15:05.326235",
                                                        "text": "We will definitely explore space.",
                                                        "username": "Oleg"
                                                    }
                                                ]
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404", description = "News not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-18T15:13:10.3448616",
                                        "error_message": "News not found with 6a268b16-85ea-491f-be9f-00f2e28d1a48",
                                        "error_status": 404
                                    }
                                    """)))
            }
    )
    ResponseNewsWithComments findCommentsByNewsId(UUID newsId, Pageable pageable);

    @Operation(
            method = "GET",
            tags = "Comment",
            description = "Get page of comments",
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
                                                             "id": "09434a73-e4e5-463b-a772-f282adef33a9",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "Best company in America.",
                                                             "username": "Nikolay",
                                                             "newsId": "6a268b16-85ea-491f-be9f-f2e28d1a4895"
                                                         },
                                                         {
                                                             "id": "0af362c7-999e-428d-91ec-703ee540c54a",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "Good news.",
                                                             "username": "Igor",
                                                             "newsId": "6a268b16-85ea-491f-be9f-f2e28d1a4895"
                                                         },
                                                         {
                                                             "id": "4d0eb5fb-6c3b-4512-9c75-7a8ce5eac4e9",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "We will definitely explore space.",
                                                             "username": "Oleg",
                                                             "newsId": "6a268b16-85ea-491f-be9f-f2e28d1a4895"
                                                         },
                                                         {
                                                             "id": "5a0d7e7c-bbac-4165-a4bd-cc9ca0b05ef7",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "It is a good car.",
                                                             "username": "Yury",
                                                             "newsId": "389ec033-0631-4c0a-825b-e9ed165104d7"
                                                         },
                                                         {
                                                             "id": "f1881042-e35c-4d99-858f-2585954240ce",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "The right direction.",
                                                             "username": "Pavel",
                                                             "newsId": "389ec033-0631-4c0a-825b-e9ed165104d7"
                                                         },
                                                         {
                                                             "id": "f36685fa-00b3-4b80-a064-97599fba9d5c",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "I would like such a car Xiaomi.",
                                                             "username": "Sveta",
                                                             "newsId": "389ec033-0631-4c0a-825b-e9ed165104d7"
                                                         },
                                                         {
                                                             "id": "190763b6-6e0c-44fd-b770-cb6283e4e272",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "Mercedes is the ideal of comfort.",
                                                             "username": "Egor",
                                                             "newsId": "0a30edeb-a294-43bc-a8b8-e5de7e2c8e08"
                                                         },
                                                         {
                                                             "id": "72e53e9c-2f06-4cda-88a2-9d926115813b",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "The best cars are German as always.",
                                                             "username": "Helena",
                                                             "newsId": "0a30edeb-a294-43bc-a8b8-e5de7e2c8e08"
                                                         },
                                                         {
                                                             "id": "8670b4d6-8c92-4119-882d-530ea13cf3a0",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "Probably this car will cost a lot of money.",
                                                             "username": "Dmitriy",
                                                             "newsId": "0a30edeb-a294-43bc-a8b8-e5de7e2c8e08"
                                                         },
                                                         {
                                                             "id": "1df46147-6686-4355-9fa2-43be5d712caa",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "I don't see the charms, the car is like a car.",
                                                             "username": "Leonid",
                                                             "newsId": "f0759f37-00ec-456f-a555-e634dc90a1f6"
                                                         },
                                                         {
                                                             "id": "d3e58657-c240-48bd-afb8-816592a22c67",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "It looks good, but I'm not a connoisseur, I don't care.",
                                                             "username": "Radion",
                                                             "newsId": "f0759f37-00ec-456f-a555-e634dc90a1f6"
                                                         },
                                                         {
                                                             "id": "0c0f2475-18de-45c7-ba3d-3db78b908c43",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "Toyota has beautiful cars.",
                                                             "username": "Marina",
                                                             "newsId": "f0759f37-00ec-456f-a555-e634dc90a1f6"
                                                         },
                                                         {
                                                             "id": "c8d8bc8c-f869-4f45-a4e4-b813cd7f342d",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "I didn't see a problem with that.",
                                                             "username": "Slava",
                                                             "newsId": "d215d55e-fe9b-4946-b55f-6b428cf7a68b"
                                                         },
                                                         {
                                                             "id": "bbeeeb25-0a04-4309-a071-e68172b27f44",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "Audi is doing great as it is.",
                                                             "username": "Sergey",
                                                             "newsId": "d215d55e-fe9b-4946-b55f-6b428cf7a68b"
                                                         },
                                                         {
                                                             "id": "443039da-b0cc-43d9-af77-c35433e03a11",
                                                             "createdBy": null,
                                                             "updatedBy": null,
                                                             "createdAt": "2024-04-18T14:15:05.326235",
                                                             "updatedAt": "2024-04-18T14:15:05.326235",
                                                             "text": "They must have come up with something terrible.",
                                                             "username": "Fedor",
                                                             "newsId": "d215d55e-fe9b-4946-b55f-6b428cf7a68b"
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
                                                     "totalElements": 15,
                                                     "size": 20,
                                                     "number": 0,
                                                     "sort": {
                                                         "empty": true,
                                                         "sorted": false,
                                                         "unsorted": true
                                                     },
                                                     "first": true,
                                                     "numberOfElements": 15,
                                                     "empty": false
                                                 }
                                            """)))
            }
    )
    Page<ResponseCommentNews> getAllComments(Pageable pageable);

    @Operation(
            method = "GET",
            tags = "Comment",
            description = "Get page of comments by filter",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Filter.class),
                            examples = @ExampleObject("""
                                    {
                                        "part": "problem"
                                    }
                                    """))),
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
                                                               "id": "c8d8bc8c-f869-4f45-a4e4-b813cd7f342d",
                                                               "createdBy": null,
                                                               "updatedBy": null,
                                                               "createdAt": "2024-04-18T14:15:05.326235",
                                                               "updatedAt": "2024-04-18T14:15:05.326235",
                                                               "text": "I didn't see a problem with that.",
                                                               "username": "Slava",
                                                               "newsId": "d215d55e-fe9b-4946-b55f-6b428cf7a68b"
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
                                                       "size": 20,
                                                       "number": 0,
                                                       "sort": {
                                                           "empty": true,
                                                           "sorted": false,
                                                           "unsorted": true
                                                       },
                                                       "first": true,
                                                       "numberOfElements": 1,
                                                       "empty": false
                                                   }
                                            """)))
            }
    )
    Page<ResponseCommentNews> findCommentsByFilter(Filter filter, Pageable pageable);

    @Operation(
            method = "POST",
            tags = "Comment",
            description = "Create comment",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateCommentDto.class),
                            examples = @ExampleObject("""
                                    {
                                        "text":"Create first comment!",
                                        "newsId":"389ec033-0631-4c0a-825b-e9ed165104d7"
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseCommentNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "5ef77878-73a0-4fbf-8c02-57aa7ab62041",
                                                "createdBy": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "updatedBy": null,
                                                "createdAt": "2024-04-18T15:29:24.59789",
                                                "updatedAt": "2024-04-18T15:29:24.59789",
                                                "text": "Create first comment!",
                                                "username": "Ivan Sidorov",
                                                "newsId": "389ec033-0631-4c0a-825b-e9ed165104d7"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404", description = "News not found.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T15:49:01.5772001",
                                                "error_message": "News not found with 389ec033-0631-4c0a-825b-e9ed165104d8",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Request data is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T15:31:06.6601526",
                                                "error_message": "{text=размер должен находиться в диапазоне от 3 до 500}",
                                                "error_status": 400
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "NewsId is incorrect.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T15:35:25.5867249",
                                                "error_message": "Incorrectly entered newsId",
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
    ResponseCommentNews createComment(CreateCommentDto commentDto);

    @Operation(
            method = "PUT",
            tags = "Comment",
            description = "Update a comment by id",
            parameters = {
                    @Parameter(name = "commentId", description = "Id of comment", example = "9725df2c-b725-4572-98e5-aa60d430c757")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCommentDto.class),
                            examples = @ExampleObject("""
                                    {
                                        "text": "It is update comment."
                                    }
                                    """))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseCommentNews.class),
                                    examples = @ExampleObject("""
                                            {
                                                "id": "9725df2c-b725-4572-98e5-aa60d430c757",
                                                "createdBy": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "updatedBy": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                "createdAt": "2024-04-18T15:36:49.345772",
                                                "updatedAt": "2024-04-18T16:02:43.289365",
                                                "text": "It is update comment.",
                                                "username": "Ivan Sidorov",
                                                "newsId": "389ec033-0631-4c0a-825b-e9ed165104d7"
                                            }
                                            """))),
                    @ApiResponse(responseCode = "404", description = "Comment not found.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:08:19.3778544",
                                                "error_message": "Comment not found with 9725df2c-b725-4572-98e5-aa60d430c758",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is incorrectly.",
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
    ResponseCommentNews updateComment(UUID commentId, UpdateCommentDto updateCommentDto);

    @Operation(
            method = "DELETE",
            tags = "Comment",
            description = "Delete a comment by commentId",
            parameters = {
                    @Parameter(name = "commentId", description = "commentId of comment", example = "9725df2c-b725-4572-98e5-aa60d430c757")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Comment deleted."),
                    @ApiResponse(responseCode = "404", description = "The endpoint has not been completed when a comment not found.",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:32:48.497019",
                                                "error_message": "Comment not found with 9725df2c-b725-4572-98e5-00aa60d430c7",
                                                "error_status": 404
                                            }
                                    """))),
                    @ApiResponse(responseCode = "400", description = "Id is incorrectly",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                    {
                                        "timestamp": "2024-04-18T16:33:22.0030552",
                                        "error_message": "UUID was entered incorrectly!",
                                        "error_status": 400
                                    }
                                    """))),
                    @ApiResponse(responseCode = "401", description = "Token is not entered.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-24T08:27:52.9444037",
                                                "error_message": "Full authentication is required to access this resource",
                                                "error_status": 401
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "Token without access.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:27:20.864849",
                                                "error_message": "Access Denied",
                                                "error_status": 403
                                            }
                                    """))),
                    @ApiResponse(responseCode = "403", description = "User can't delete comment other user.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                                            {
                                                "timestamp": "2024-04-18T16:47:03.6009148",
                                                "error_message": "You have no right to change the data of other users!",
                                                "error_status": 403
                                            }
                                    """)))
            }
    )
    void deleteCommentById(UUID commentId);
}