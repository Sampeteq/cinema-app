package com.cinema.users.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<JpaUser, UUID> {

    Optional<JpaUser> getByMail(String mail);

    Optional<JpaUser> getByPasswordResetToken(UUID passwordResetToken);
}