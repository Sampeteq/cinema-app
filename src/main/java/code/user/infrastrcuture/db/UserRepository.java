package code.user.infrastrcuture.db;

import code.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    User add(User user);
    Optional<User> readyByMail(String mail);
    boolean existsByMail(String mail);
}
