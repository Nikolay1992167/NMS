package ru.clevertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByNewsId(UUID newsId, Pageable pageable);

    Page<Comment> findAll(Pageable pageable);

    Page<Comment> findAll(Specification<Comment> specification, Pageable pageable);
}