package ru.clevertec.mapper;

import ru.clevertec.dto.request.CreateCommentDto;
import ru.clevertec.dto.request.UpdateCommentDto;
import ru.clevertec.dto.response.ResponseComment;
import ru.clevertec.dto.response.ResponseCommentNews;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.dto.response.ResponseNewsWithComments;
import ru.clevertec.entity.Comment;
import ru.clevertec.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    /**
     * Converts List<Comment> comments to List<ResponseComment> containing information about the comment.
     *
     * @param comments list of object {@link Comment}.
     * @return List<ResponseComment> with information about the comment.
     */
    List<ResponseComment> toResponses(List<Comment> comments);

    /**
     * Converts {@link Comment} to {@link ResponseCommentNews} containing information about the comment.
     *
     * @param comment object {@link Comment}.
     * @return {@link ResponseCommentNews} with information about the comment.
     */
    @Mapping(target = "newsId", source = "news.id")
    ResponseCommentNews toResponseWithNewsId(Comment comment);

    /**
     * Converts {@link CreateCommentDto} with comment information to {@link Comment}
     *
     * @param commentDto object {@link CreateCommentDto} containing data for creating a comment.
     * @return {@link Comment}.
     */
    @Mapping(target = "news", source = "newsId", qualifiedByName = "newsId")
    Comment toComment(CreateCommentDto commentDto);

    /**
     * Updates {@link Comment} by {@link UpdateCommentDto} with information about the comment.
     *
     * @param update object {@link UpdateCommentDto} containing data for updating the comment.
     * @param comment updated {@link Comment}.
     * @return {@link Comment}.
     */
    @Mapping(target = "text", source = "text")
    Comment update(UpdateCommentDto update, @MappingTarget Comment comment);

    /**
     * Converts {@link ResponseNews} with information to {@link ResponseNewsWithComments} with list of comments.
     *
     * @param response object {@link ResponseNews} containing data the news.
     * @param responses list objects {@link ResponseComment}.
     * @return {@link ResponseNewsWithComments}.
     */
    @Mapping(target = "comments", source = "responses")
    ResponseNewsWithComments toNewsWithCommentsResponse(ResponseNews response, List<ResponseComment> responses);

    /**
     * Creates a new {@link News} object with ID.
     *
     * @param newsId of news.
     * @return new object {@link News} with ID.
     */
    @Named("newsId")
    default News getNews(UUID newsId) {
        News news = new News();
        news.setId(newsId);
        return news;
    }
}