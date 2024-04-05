package com.cinema.users.infrastructure;

import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

interface JpaUserRepository extends JpaRepository<User, Long> {

    Optional<User> getByMail(String mail);

    Optional<User> getByPasswordResetToken(UUID passwordResetToken);
}

@Repository
@RequiredArgsConstructor
class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> getByMail(String mail) {
        return jpaUserRepository.getByMail(mail);
    }

    @Override
    public Optional<User> getByPasswordResetToken(UUID passwordResetToken) {
        return jpaUserRepository.getByPasswordResetToken(passwordResetToken);
    }
}
