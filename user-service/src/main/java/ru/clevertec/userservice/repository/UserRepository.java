package ru.clevertec.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.userservice.enums.Status;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.model.User_;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {User_.ROLES})
    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {User_.ROLES})
    Optional<User> findUserFetchRolesById(UUID userId);
}