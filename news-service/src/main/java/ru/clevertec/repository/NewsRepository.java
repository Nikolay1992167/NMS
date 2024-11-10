package ru.clevertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {

    @EntityGraph(attributePaths = "comments")
    Optional<News> findById(UUID newsId);

    Page<News> findAll(Pageable pageable);

    Page<News> findAll(Specification<News> specification, Pageable pageable);
}