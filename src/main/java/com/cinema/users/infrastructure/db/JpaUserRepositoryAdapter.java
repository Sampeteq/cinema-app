package com.cinema.users.infrastructure.db;

import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserMapper mapper;

    @Override
    public User save(User user) {
        return mapper.toDomain(jpaUserRepository.save(mapper.toJpa(user)));
    }

    @Override
    public Optional<User> getByMail(String mail) {
        return jpaUserRepository.getByMail(mail).map(mapper::toDomain);
    }

    @Override
    public Optional<User> getByPasswordResetToken(UUID passwordResetToken) {
        return jpaUserRepository.getByPasswordResetToken(passwordResetToken).map(mapper::toDomain);
    }
}
