package ru.clevertec.validation;

import ru.clevertec.dto.request.JwtUser;
import ru.clevertec.enams.ErrorMessage;
import ru.clevertec.entity.Comment;
import ru.clevertec.exception.handling.exception.AccessException;
import ru.clevertec.exception.handling.exception.NotFoundException;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentValidator {
    private final CommentRepository commentRepository;

    public Boolean isOwnerRightByChange(UUID commentId) {
        JwtUser user = AuthUtil.getUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND.getMessage() + commentId));
        if (!isAdmin(user) &&
                (!isSubscriber(user) || !user.getId().equals(comment.getCreatedBy()))) {
            throw new AccessException(ErrorMessage.ERROR_CHANGE.getMessage());
        }
        return true;
    }

    private boolean isAdmin(JwtUser user) {
        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    private boolean isSubscriber(JwtUser user) {
        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("SUBSCRIBER"));
    }
}