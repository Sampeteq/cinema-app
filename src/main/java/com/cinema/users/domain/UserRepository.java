package com.cinema.users.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User add(User user);
    Optional<User> readyByMail(String mail);
    Optional<User> readByPasswordResetToken(UUID passwordResetToken);
    boolean existsByMail(String mail);
}
