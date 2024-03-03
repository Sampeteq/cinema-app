package com.cinema.users;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> getByMail(String mail);

    Optional<User> getByPasswordResetToken(UUID passwordResetToken);
}
