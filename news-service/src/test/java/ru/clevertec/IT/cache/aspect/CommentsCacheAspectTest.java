package ru.clevertec.IT.cache.aspect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import ru.clevertec.cache.aspect.CommentsCacheAspect;
import ru.clevertec.dto.request.CreateCommentDto;
import ru.clevertec.dto.request.JwtUser;
import ru.clevertec.dto.response.ResponseCommentNews;
import ru.clevertec.entity.Comment;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.service.CommentService;
import ru.clevertec.util.IntegrationTest;
import ru.clevertec.util.PostgresqlTestContainer;
import ru.clevertec.util.testdata.CommentTestData;
import ru.clevertec.util.testdata.JwtUserTestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.util.init.InitData.ID_COMMENT;

@IntegrationTest
@TestPropertySource(properties = {
        "spring.cache.algorithm=lfu",
        "spring.cache.size=2",
})
class CommentsCacheAspectTest extends PostgresqlTestContainer {

    @Autowired
    private CommentService service;

    @MockBean
    private CommentRepository repository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Autowired
    private CommentsCacheAspect commentsCacheAspect;

    @AfterEach
    void clear() {
        commentsCacheAspect.clear();
    }

    @Test
    void getCached() {
        // given
        Comment comment = CommentTestData.getComment();

        doReturn(Optional.of(comment))
                .when(repository).findById(ID_COMMENT);

        // when
        service.findCommentById(ID_COMMENT);
        service.findCommentById(ID_COMMENT);

        // then
        verify(repository, times(1))
                .findById(ID_COMMENT);
    }

    @Test
    void deleteFromCache() {
        // given
        Comment comment = CommentTestData.getComment();
        ResponseCommentNews expected = CommentTestData.getResponseCommentNews();
        JwtUser jwtUser = JwtUserTestData.getJwtUserValidationCommentAdmin();

        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        doReturn(Optional.of(comment), Optional.of(comment), Optional.empty())
                .when(repository).findById(ID_COMMENT);

        // when
        service.findCommentById(ID_COMMENT);
        service.deleteCommentById(ID_COMMENT);

        // then
        ResponseCommentNews actual = (ResponseCommentNews) commentsCacheAspect.getCached(null, ID_COMMENT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void putToCache() {
        // given
        Comment comment = CommentTestData.getComment();
        JwtUser jwtUser = JwtUserTestData.getJwtUserValidationCommentAdmin();
        CreateCommentDto commentDto = CommentTestData.getCommentDto();

        when(authentication.getPrincipal())
                .thenReturn(jwtUser);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        doReturn(comment)
                .when(repository).saveAndFlush(any(Comment.class));

        // when
        service.createComment(commentDto);
        service.findCommentById(ID_COMMENT);

        // then
        verify(repository, never())
                .findById(ID_COMMENT);
    }
}