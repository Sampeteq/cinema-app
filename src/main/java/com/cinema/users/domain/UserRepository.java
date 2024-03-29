package com.cinema.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByMail(String mail);

    Optional<User> getByPasswordResetToken(UUID passwordResetToken);
}
