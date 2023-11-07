package com.cinema.users.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User add(User user);
    Optional<User> getByMail(String mail);
    Optional<User> getByPasswordResetToken(UUID passwordResetToken);
    boolean existsByMail(String mail);
}
