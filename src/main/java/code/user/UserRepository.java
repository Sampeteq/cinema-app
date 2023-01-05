package code.user;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRepository {

    void add(User user);

    Optional<User> getById(String username);

    boolean existsById(String username);
}

interface JpaUserRepository extends JpaRepository<User, String> {

}

@AllArgsConstructor
class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void add(User user) {
        jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> getById(String username) {
        return jpaUserRepository.findById(username);
    }

    @Override
    public boolean existsById(String username) {
        return jpaUserRepository.existsById(username);
    }
}
