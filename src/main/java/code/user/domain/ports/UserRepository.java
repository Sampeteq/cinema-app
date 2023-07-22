package code.user.domain.ports;

import code.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User add(User user);
    Optional<User> readyByMail(String mail);
    Optional<User> readByPasswordResetToken(UUID passwordResetToken);
    boolean existsByMail(String mail);
}
