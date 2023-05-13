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
    public Optional<User> readyByMail(String mail) {
        return jpaUserRepository.readByMail(mail);
    }

    @Override
    public boolean existsByMail(String mail) {
        return jpaUserRepository.existsByMail(mail);
    }
}

interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> readByMail(String mail);
    boolean existsByMail(String mail);
}
