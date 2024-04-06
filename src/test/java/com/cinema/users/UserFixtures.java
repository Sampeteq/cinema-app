package com.cinema.users;

import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public final class UserFixtures {

    public static final String MAIL = "user1@mail.com";

    public static final String PASSWORD = "password1";

    private UserFixtures() {
    }

    public static User createUser() {
        return new User(null, MAIL, PASSWORD, UserRole.COMMON, null);
    }

    public static User createUser(UserRole role) {
        return new User(null, MAIL, PASSWORD, role, null);
    }

    public static User createUser(PasswordEncoder passwordEncoder) {
        return new User(null, MAIL, passwordEncoder.encode(PASSWORD), UserRole.COMMON, null);
    }

    public static User createUser(String mail) {
        return new User(null, mail, PASSWORD, UserRole.COMMON, null);
    }

    public static User createUser(String mail, String password) {
        return new User(null, mail, password, UserRole.COMMON, null);
    }

    public static User createUser(UUID passwordResetToken) {
        var user = new User(null, MAIL, PASSWORD, UserRole.COMMON, null);
        user.assignPasswordResetToken(passwordResetToken);
        return user;
    }
}
