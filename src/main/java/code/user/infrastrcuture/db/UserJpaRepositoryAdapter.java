package code.user.infrastrcuture.db;

import code.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public boolean existsByMail(String mail) {
        return jpaUserRepository.existsByMail(mail);
    }
}

interface UserJpaRepository extends JpaRepository<User, String> {
    Optional<User> readByMail(String mail);
    boolean existsByMail(String mail);
}
