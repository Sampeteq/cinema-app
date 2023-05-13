package code.user.domain;

import java.util.Optional;

public interface UserRepository {
    User add(User user);
    Optional<User> readyByMail(String username);
    boolean existsByMail(String mail);
}
