package com.cinema.users.infrastructure;

import com.cinema.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);

    Optional<User> findByPasswordResetToken(UUID passwordResetToken);
}
