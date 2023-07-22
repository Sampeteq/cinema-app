package code.user.infrastrcuture.db;

import code.user.domain.User;
import code.user.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserJpaRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaUserRepository;

    @Override
    public User add(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> readyByMail(String mail) {
        return jpaUserRepository.readByMail(mail);
    }

    @Override
    public Optional<User> readByPasswordResetToken(UUID passwordResetToken) {
        return jpaUserRepository.readByPasswordResetToken(passwordResetToken);
    }

    @Override
    public boolean existsByMail(String mail) {
        return jpaUserRepository.existsByMail(mail);
    }
}

interface UserJpaRepository extends JpaRepository<User, String> {
    Optional<User> readByMail(String mail);
    Optional<User> readByPasswordResetToken(UUID passwordResetToken);
    boolean existsByMail(String mail);
}
