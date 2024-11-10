package ru.clevertec.IT.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.dto.request.CreateCommentDto;
import ru.clevertec.dto.request.JwtUser;
import ru.clevertec.dto.response.ResponseCommentNews;
import ru.clevertec.entity.Comment;
import ru.clevertec.entity.News;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.service.CommentService;
import ru.clevertec.util.IntegrationTest;
import ru.clevertec.util.PostgresqlTestContainer;
import ru.clevertec.util.RedisTestConfig;
import ru.clevertec.util.RedisTestContainer;
import ru.clevertec.util.testdata.CommentTestData;
import ru.clevertec.util.testdata.JwtUserTestData;
import ru.clevertec.util.testdata.NewsTestData;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.util.init.InitData.ID_COMMENT;

@IntegrationTest
@Import(RedisTestConfig.class)
@Sql("classpath:sql/insert_data_for_cache_tests.sql")
class CacheRedisTest extends PostgresqlTestContainer implements RedisTestContainer {

    @Autowired
    private CommentService service;

    @MockBean
    private CommentRepository repository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Autowired
    private CacheManager manager;

    @AfterEach
    void clear() {
        Objects.requireNonNull(manager.getCache("ResponseCommentNews"))
                .clear();
    }

    @Test
    void shouldReturnExpectedValueFindById() {
        // given
        News news = NewsTestData.getNews();
        Comment cached = CommentTestData.getComment();
        cached.setNews(news);
        ResponseCommentNews expected = CommentTestData.getResponseCommentNews();
        UUID id = expected.id();

        doReturn(Optional.of(cached))
                .when(repository).findById(id);

        // when
        service.findCommentById(id);
        ResponseCommentNews actual = service.findCommentById(id);

        // then
        verify(repository, times(1))
                .findById(id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldThrowExceptionFindById() {
        // given
        UUID id = ID_COMMENT;

        doReturn(Optional.empty(), Optional.empty())
                .when(repository).findById(id);

        // when, then
        assertThatThrownBy(() -> service.findCommentById(id)).isExactlyInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> service.findCommentById(id)).isExactlyInstanceOf(NotFoundException.class);

        verify(repository, times(2)).findById(id);
    }

    @Test
    void shouldSuccessfullyDelete() {
        // given
        Comment cached = CommentTestData.getComment();
        JwtUser jwtUser = JwtUserTestData.getJwtUserValidationCommentAdmin();

        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        doReturn(Optional.of(cached), Optional.of(cached), Optional.empty())
                .when(repository).findById(ID_COMMENT);
        doNothing()
                .when(repository).deleteById(ID_COMMENT);

        // when
        service.findCommentById(ID_COMMENT);
        service.deleteCommentById(ID_COMMENT);

        // then
        assertThatThrownBy(() -> service.findCommentById(ID_COMMENT)).isInstanceOf(NotFoundException.class);
        verify(repository, times(3))
                .findById(ID_COMMENT);
    }

    @Test
    void shouldSuccessfullySaveComment() {
        // given
        JwtUser jwtUser = JwtUserTestData.getJwtUser();
        Comment comment = CommentTestData.getComment();
        CreateCommentDto commentDto = CommentTestData.getCommentDto();
        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(repository.saveAndFlush(any(Comment.class)))
                .thenReturn(comment);

        // when
        service.createComment(commentDto);
        ResponseCommentNews byId = service.findCommentById(ID_COMMENT);

        // then
        assertThat(byId).extracting(
                        "text", "username")
                .containsExactly(
                        comment.getText(), comment.getUsername());
        verify(repository, times(0))
                .findById(ID_COMMENT);
    }
}