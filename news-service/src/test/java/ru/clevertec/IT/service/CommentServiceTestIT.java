package ru.clevertec.IT.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.dto.request.CreateCommentDto;
import ru.clevertec.dto.request.Filter;
import ru.clevertec.dto.request.JwtUser;
import ru.clevertec.dto.request.UpdateCommentDto;
import ru.clevertec.dto.response.ResponseCommentNews;
import ru.clevertec.dto.response.ResponseNewsWithComments;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.service.CommentService;
import ru.clevertec.util.IntegrationTest;
import ru.clevertec.util.PostgresqlTestContainer;
import ru.clevertec.util.testdata.CommentTestData;
import ru.clevertec.util.testdata.JwtUserTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static ru.clevertec.util.init.InitData.DEFAULT_PAGE_REQUEST_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_COMMENT;
import static ru.clevertec.util.init.InitData.ID_COMMENT_FOR_IT;
import static ru.clevertec.util.init.InitData.ID_COMMENT_FOR_IT_DELETE;
import static ru.clevertec.util.init.InitData.ID_NEWS;
import static ru.clevertec.util.init.InitData.ID_NEWS_FOR_IT;
import static ru.clevertec.util.init.InitData.TEXT_COMMENT;

@IntegrationTest
@Sql("classpath:sql/insert_data_for_IT.sql")
public class CommentServiceTestIT extends PostgresqlTestContainer {

    @Autowired
    private CommentService commentService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Test
    void shouldReturnExpectedComment() {
        // given, when
        ResponseCommentNews actual = commentService.findCommentById(ID_COMMENT_FOR_IT);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(ID_COMMENT_FOR_IT);
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        // given, when, then
        assertThatThrownBy(() -> commentService.findCommentById(ID_COMMENT))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(ErrorMessage.COMMENT_NOT_FOUND.getMessage() + ID_COMMENT);
    }

    @Test
    void shouldReturnExpectedCommentForFindByNewsId() {
        // given, when
        ResponseNewsWithComments actual = commentService.findCommentsByNewsId(ID_NEWS_FOR_IT, DEFAULT_PAGE_REQUEST_FOR_IT);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(ID_NEWS_FOR_IT);
    }

    @Test
    void shouldThrowExceptionWhenNewsForCommentNotFound() {
        // given, when, then
        assertThatThrownBy(() -> commentService.findCommentsByNewsId(ID_NEWS, DEFAULT_PAGE_REQUEST_FOR_IT))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(ErrorMessage.NEWS_NOT_FOUND.getMessage() + ID_NEWS);
    }

    @Test
    void shouldReturnCommentsOnPage() {
        // given, when
        Page<ResponseCommentNews> allComments = commentService.getAllComments(DEFAULT_PAGE_REQUEST_FOR_IT);

        // then
        assertThat(allComments).isNotNull();
        assertThat(allComments.getContent().size()).isEqualTo(9);
    }

    @Test
    void shouldReturnCommentByFilter() {
        // given
        Filter filter = Filter.builder()
                .part("is")
                .build();

        // when
        Page<ResponseCommentNews> actual = commentService.findCommentsByFilter(filter, DEFAULT_PAGE_REQUEST_FOR_IT);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getContent().size()).isEqualTo(3);
    }

    @Test
    void shouldReturnCreatedComment() {
        // given
        CreateCommentDto createCommentDto = CommentTestData.getCommentDtoIT();
        JwtUser jwtUser = JwtUserTestData.getJwtUser();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when
        ResponseCommentNews actual = commentService.createComment(createCommentDto);

        // then
        assertThat(actual.id()).isNotNull();
        assertThat(actual.text()).isEqualTo(TEXT_COMMENT);
        assertThat(actual.newsId()).isEqualTo(ID_NEWS_FOR_IT);
    }

    @Test
    void shouldThrowExceptionWhenNewsNotFound() {
        // given
        CreateCommentDto createCommentDto = CommentTestData.getCommentDtoITWithIDNewsNotExist();
        JwtUser jwtUser = JwtUserTestData.getJwtUser();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when, then
        assertThatThrownBy(() -> commentService.createComment(createCommentDto))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessageContaining(ErrorMessage.NEWS_NOT_FOUND.getMessage() + createCommentDto.newsId());
    }

    @Test
    void shouldReturnUpdatedComment() {
        // given
        UpdateCommentDto updateCommentDto = CommentTestData.getUpdateCommentDto();
        JwtUser jwtUser = JwtUserTestData.getJwtUserValidationCommentAdmin();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when
        ResponseCommentNews actual = commentService.updateCommentById(ID_COMMENT_FOR_IT, updateCommentDto);

        // then
        assertThat(actual.id()).isEqualTo(ID_COMMENT_FOR_IT);
        assertThat(actual.text()).isEqualTo(updateCommentDto.text());
    }

    @Test
    void shouldThrowExceptionWhenUpdateCommentNotFound() {
        // given
        UpdateCommentDto updateCommentDto = CommentTestData.getUpdateCommentDto();
        JwtUser jwtUser = JwtUserTestData.getJwtUserValidationCommentAdmin();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when, then
        assertThatThrownBy(() -> commentService.updateCommentById(ID_COMMENT, updateCommentDto))
                .isExactlyInstanceOf(NotFoundException.class)
                .hasMessageContaining(ErrorMessage.COMMENT_NOT_FOUND.getMessage() + ID_COMMENT);
    }

    @Test
    void shouldDeleteCommentWhenUserHasRight() {
        // given
        JwtUser jwtUser = JwtUserTestData.getJwtUserValidationCommentAdmin();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when, then
        assertThatNoException().isThrownBy(() -> commentService.deleteCommentById(ID_COMMENT_FOR_IT_DELETE));
    }
}