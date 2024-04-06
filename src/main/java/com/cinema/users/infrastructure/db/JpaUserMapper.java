package com.cinema.users.infrastructure.db;

import com.cinema.users.domain.User;

public class JpaUserMapper {

    public JpaUser toJpa(User user) {
        return new JpaUser(
                user.getId(),
                user.getMail(),
                user.getPassword(),
                user.getRole(),
                user.getPasswordResetToken()
        );
    }

    public User toDomain(JpaUser user) {
        return new User(
                user.getId(),
                user.getMail(),
                user.getPassword(),
                user.getRole(),
                user.getPasswordResetToken()
        );
    }
}
