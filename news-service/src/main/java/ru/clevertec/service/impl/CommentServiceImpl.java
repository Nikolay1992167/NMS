package ru.clevertec.service.impl;

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
import ru.clevertec.service.CommentService;
import ru.clevertec.service.NewsService;
import ru.clevertec.util.AuthUtil;
import ru.clevertec.validation.CommentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentValidator commentValidator;

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final NewsService newsService;

    @Override
    @Cacheable(value = "ResponseCommentNews", key = "#commentId")
    public ResponseCommentNews findCommentById(UUID commentId) {
        return commentRepository.findById(commentId)
                .map(commentMapper::toResponseWithNewsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage() + commentId));
    }

    @Override
    @Cacheable(value = "ResponseNewsWithComments", key = "#result.id()")
    public ResponseNewsWithComments findCommentsByNewsId(UUID newsId, Pageable pageable) {
        ResponseNews responseNews = newsService.findNewsById(newsId);
        List<ResponseComment> responseComments = commentMapper.toResponses(commentRepository.findAllByNewsId(newsId, pageable));
        return commentMapper.toNewsWithCommentsResponse(responseNews, responseComments);
    }

    @Override
    public Page<ResponseCommentNews> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(commentMapper::toResponseWithNewsId);
    }

    @Override
    public Page<ResponseCommentNews> findCommentsByFilter(Filter filter, Pageable pageable) {
        return Optional.ofNullable(filter.part())
                .map(part -> "%" + part + "%")
                .map(part -> (Specification<Comment>) (root, query, cb) -> cb.like(root.get("text"), part))
                .map(spec -> commentRepository.findAll(spec, pageable))
                .orElseGet(() -> commentRepository.findAll(pageable))
                .map(commentMapper::toResponseWithNewsId);
    }

    @Override
    @Transactional
    @CachePut(value = "ResponseCommentNews", key = "#result.id()")
    public ResponseCommentNews createComment(CreateCommentDto commentDto) {
        JwtUser user = AuthUtil.getUser();
        try {
            return Optional.of(commentDto)
                    .map(commentMapper::toComment)
                    .map(comment -> {
                        comment.setCreatedBy(user.getId());
                        comment.setUsername(user.getFirstName() + " " + user.getLastName());
                        return commentRepository.saveAndFlush(comment);
                    })
                    .map(commentMapper::toResponseWithNewsId)
                    .orElseThrow();
        } catch (DataAccessException exception) {
            throw new NotFoundException(ErrorMessage.NEWS_NOT_FOUND.getMessage() + commentDto.newsId());
        }
    }

    @Override
    @Transactional
    @CachePut(value = "ResponseCommentNews", key = "#result.id()")
    public ResponseCommentNews updateCommentById(UUID commentId, UpdateCommentDto updateCommentDto) {
        UUID userId = AuthUtil.getId();
        if (commentValidator.isOwnerRightByChange(commentId)) {
            return commentRepository.findById(commentId)
                    .map(current -> {
                        Comment updatedComment = commentMapper.update(updateCommentDto, current);
                        updatedComment.setUpdatedBy(userId);
                        return commentRepository.saveAndFlush(updatedComment);
                    })
                    .map(commentMapper::toResponseWithNewsId)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage() + commentId));
        }
        return null;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "ResponseCommentNews", key = "#commentId"),
            @CacheEvict(value = "ResponseNewsWithComments", key = "#commentId")
    })
    public void deleteCommentById(UUID commentId) {
        if (commentValidator.isOwnerRightByChange(commentId)) {
            commentRepository.deleteById(commentId);
        }
    }
}