package ru.clevertec.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.dto.request.CreateCommentDto;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.request.JwtUser;
import ru.clevertec.dto.request.UpdateCommentDto;
import ru.clevertec.dto.response.ResponseComment;
import ru.clevertec.dto.response.ResponseCommentNews;
import ru.clevertec.dto.response.ResponseNews;
import ru.clevertec.dto.response.ResponseNewsWithComments;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.entity.Comment;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.mapper.CommentMapper;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.service.NewsService;
import ru.clevertec.util.testdata.CommentTestData;
import ru.clevertec.util.testdata.FilterTestData;
import ru.clevertec.util.testdata.JwtUserTestData;
import ru.clevertec.util.testdata.NewsTestData;
import ru.clevertec.validation.CommentValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.util.init.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_COMMENT;
import static ru.clevertec.util.init.InitData.ID_NEWS;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentValidator commentValidator;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsService newsService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Nested
    class FindCommentById {

        @Test
        void shouldReturnExpectedValue() {
            // given
            UUID commentId = ID_COMMENT;
            Comment comment = CommentTestData.getComment();
            ResponseCommentNews expected = CommentTestData.getResponseCommentNews();
            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.of(comment));
            when(commentMapper.toResponseWithNewsId(comment))
                    .thenReturn(expected);
            // when
            ResponseCommentNews actual = commentService.findCommentById(commentId);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldThrowExceptionWhenNewsIsNotExist() {
            // given
            UUID commentId = UUID.randomUUID();

            // when, then
            assertThatThrownBy(() -> commentService.findCommentById(commentId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.COMMENT_NOT_FOUND.getMessage() + commentId);
        }
    }

    @Nested
    class FindCommentsByNewsId {

        @Test
        void shouldReturnExpectedValue() {
            // given
            UUID newsId = ID_NEWS;
            ResponseNews responseNews = NewsTestData.getResponseNews();
            List<ResponseComment> responseComments = List.of(CommentTestData.getResponseComment());
            ResponseNewsWithComments expectedResponse = CommentTestData.getResponseNewsWithComments();
            when(newsService.findNewsById(newsId))
                    .thenReturn(responseNews);
            when(commentRepository.findAllByNewsId(newsId, DEFAULT_PAGE_REQUEST_FOR_IT))
                    .thenReturn(new ArrayList<>());
            when(commentMapper.toResponses(anyList()))
                    .thenReturn(responseComments);
            when(commentMapper.toNewsWithCommentsResponse(responseNews, responseComments))
                    .thenReturn(expectedResponse);

            // when
            ResponseNewsWithComments actualResponse = commentService.findCommentsByNewsId(newsId, DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }

    @Nested
    class GetAllComments {

        @Test
        void shouldReturnPageOfResponseCommentNews() {
            // given
            int expectedSize = 1;
            List<Comment> commentList = List.of(CommentTestData.getComment());
            Page<Comment> page = new PageImpl<>(commentList);
            when(commentRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<ResponseCommentNews> actual = commentService.getAllComments(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual.getTotalElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldCheckEmpty() {
            // given
            Page<Comment> page = new PageImpl<>(List.of());
            when(commentRepository.findAll(any(PageRequest.class)))
                    .thenReturn(page);

            // when
            Page<ResponseCommentNews> actual = commentService.getAllComments(DEFAULT_PAGE_REQUEST_FOR_IT);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class FindCommentsByFilter {

        @Test
        void shouldReturnPageOfResponseCommentNews() {
            // given
            Pageable pageable = Pageable.unpaged();
            Filter filter = FilterTestData.getFilter();
            List<Comment> comments = List.of(CommentTestData.getComment());
            PageImpl<Comment> commentPage = new PageImpl<>(comments, pageable, 1);
            List<ResponseCommentNews> expectedComments = List.of(CommentTestData.getResponseCommentNews());

            doReturn(commentPage)
                    .when(commentRepository).findAll(any(Specification.class), eq(pageable));
            IntStream.range(0, comments.size())
                    .forEach(i -> doReturn(expectedComments.get(i))
                            .when(commentMapper).toResponseWithNewsId(comments.get(i)));

            // when
            List<ResponseCommentNews> actualComments =
                    commentService
                            .findCommentsByFilter(filter, pageable)
                            .getContent();

            // then
            assertThat(actualComments).isEqualTo(expectedComments);
        }

        @Test
        void checkEmpty() {
            // given
            Pageable pageable = Pageable.unpaged();
            Filter emptyFilter = new Filter(null);
            List<Comment> comments = List.of(CommentTestData.getComment());
            PageImpl<Comment> commentPage = new PageImpl<>(comments, pageable, 2);
            List<ResponseCommentNews> expectedComments = List.of(CommentTestData.getResponseCommentNews());

            doReturn(commentPage)
                    .when(commentRepository).findAll(pageable);
            IntStream.range(0, comments.size())
                    .forEach(i -> doReturn(expectedComments.get(i))
                            .when(commentMapper).toResponseWithNewsId(comments.get(i)));

            // when
            List<ResponseCommentNews> actualComments =
                    commentService
                            .findCommentsByFilter(emptyFilter, pageable)
                            .getContent();

            // then
            assertThat(actualComments).isEqualTo(expectedComments);
        }
    }

    @Nested
    class CreateComment {

        @Test
        void shouldReturnExpectedValue() {
            // given
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            Comment comment = CommentTestData.getComment();
            CreateCommentDto commentDto = CommentTestData.getCommentDto();
            ResponseCommentNews expected = CommentTestData.getResponseCommentNews();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(commentMapper.toComment(commentDto))
                    .thenReturn(comment);
            when(commentRepository.saveAndFlush(any(Comment.class)))
                    .thenReturn(comment);
            when(commentMapper.toResponseWithNewsId(comment))
                    .thenReturn(expected);

            // when
            ResponseCommentNews actual = commentService.createComment(commentDto);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldThrowException() {
            // given
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            Comment comment = CommentTestData.getComment();
            CreateCommentDto commentDto = CommentTestData.getCommentDto();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(commentMapper.toComment(commentDto))
                    .thenReturn(comment);
            when(commentRepository.saveAndFlush(any(Comment.class)))
                    .thenThrow(new DataAccessException("Exception!") {
                    });

            // when, then
            assertThatThrownBy(() -> commentService.createComment(commentDto))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.NEWS_NOT_FOUND.getMessage() + commentDto.newsId());
        }
    }

    @Nested
    class UpdateCommentById {

        @Test
        void shouldReturnNull() {
            // given
            UUID commentId = ID_COMMENT;
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            UpdateCommentDto updateCommentDto = CommentTestData.getUpdateCommentDto();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(commentValidator.isOwnerRightByChange(commentId))
                    .thenReturn(Boolean.FALSE);

            // when
            ResponseCommentNews actual = commentService.updateCommentById(commentId, updateCommentDto);

            // then
            assertThat(actual).isNull();
        }

        @Test
        void shouldReturnExpectedValue() {
            // given
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            UUID commentId = ID_COMMENT;
            UpdateCommentDto updateCommentDto = CommentTestData.getUpdateCommentDto();
            Comment comment = CommentTestData.getComment();
            ResponseCommentNews response = CommentTestData.getResponseCommentNews();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(commentValidator.isOwnerRightByChange(commentId))
                    .thenReturn(true);
            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.of(comment));
            when(commentMapper.update(updateCommentDto, comment))
                    .thenReturn(comment);
            when(commentRepository.saveAndFlush(comment))
                    .thenReturn(comment);
            when(commentMapper.toResponseWithNewsId(comment))
                    .thenReturn(response);

            // when
            ResponseCommentNews actualResponse = commentService.updateCommentById(commentId, updateCommentDto);

            // then
            assertEquals(response, actualResponse);
        }

        @Test
        void shouldThrowException() {
            // given
            UUID commentId = ID_COMMENT;
            JwtUser jwtUser = JwtUserTestData.getJwtUser();
            UpdateCommentDto updateCommentDto = CommentTestData.getUpdateCommentDto();
            when(authentication.getPrincipal())
                    .thenReturn(jwtUser);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(commentValidator.isOwnerRightByChange(commentId))
                    .thenReturn(Boolean.TRUE);
            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> commentService.updateCommentById(commentId, updateCommentDto))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining(ErrorMessage.COMMENT_NOT_FOUND.getMessage() + commentId);
        }
    }

    @Nested
    class DeleteCommentById {

        @Test
        void shouldDeleteCommentWhenUserIsOwner() {
            // given
            UUID commentId = ID_COMMENT;
            when(commentValidator.isOwnerRightByChange(commentId))
                    .thenReturn(Boolean.TRUE);
            doNothing().when(commentRepository).deleteById(commentId);

            // when
            commentService.deleteCommentById(commentId);

            // then
            verify(commentValidator, times(1)).isOwnerRightByChange(commentId);
            verify(commentRepository, times(1)).deleteById(commentId);
        }

        @Test
        public void testDeleteCommentByIdWhenNotOwner() {
            // given
            UUID commentId = ID_COMMENT;
            when(commentValidator.isOwnerRightByChange(commentId))
                    .thenReturn(Boolean.FALSE);

            // when
            commentService.deleteCommentById(commentId);

            // then
            verify(commentValidator, times(1)).isOwnerRightByChange(commentId);
            verify(commentRepository, times(0)).deleteById(commentId);
        }
    }
}