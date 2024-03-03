package com.cinema.users.infrastructure;

import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class JpaUserRepositoryAdapter implements UserRepository {

    interface JpaUserRepository extends JpaRepository<User, Long> {
        Optional<User> findByMail(String mail);
        Optional<User> findByPasswordResetToken(UUID passwordResetToken);
    }

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> getByMail(String mail) {
        return jpaUserRepository.findByMail(mail);
    }

    @Override
    public Optional<User> getByPasswordResetToken(UUID passwordResetToken) {
        return jpaUserRepository.findByPasswordResetToken(passwordResetToken);
    }
}
