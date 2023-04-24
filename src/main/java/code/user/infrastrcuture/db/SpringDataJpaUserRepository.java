package code.user.infrastrcuture.db;

import code.user.domain.User;
import code.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User add(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> readByUsername(String username) {
        return jpaUserRepository.readByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }
}

interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> readByUsername(String username);
    boolean existsByUsername(String username);
}
