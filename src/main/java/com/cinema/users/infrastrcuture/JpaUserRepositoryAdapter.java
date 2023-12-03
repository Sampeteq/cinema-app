package com.cinema.users.infrastrcuture;

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

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User add(User user) {
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

    @Override
    public boolean existsByMail(String mail) {
        return jpaUserRepository.existsByMail(mail);
    }
}

interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> findByMail(String mail);
    Optional<User> findByPasswordResetToken(UUID passwordResetToken);
    boolean existsByMail(String mail);
}
