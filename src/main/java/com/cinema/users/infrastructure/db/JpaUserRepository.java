package com.cinema.users.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<JpaUser, Long> {

    Optional<JpaUser> getByMail(String mail);

    Optional<JpaUser> getByPasswordResetToken(UUID passwordResetToken);
}