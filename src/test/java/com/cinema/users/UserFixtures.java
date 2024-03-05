package com.cinema.users;

import com.cinema.users.domain.User;
import com.cinema.users.infrastructure.UserCreateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public final class UserFixtures {

    public static final String MAIL = "user1@mail.com";

    public static final String PASSWORD = "password1";

    private UserFixtures() {
    }

    public static UserCreateRequest createUserCreateRequest() {
        return new UserCreateRequest(
                MAIL,
                PASSWORD
        );
    }

    public static UserCreateRequest createUserCreateRequest(String mail) {
        return new UserCreateRequest(
                mail,
                PASSWORD
        );
    }

    public static User createUser() {
        return new User(MAIL, PASSWORD, User.Role.COMMON);
    }

    public static User createUser(User.Role role) {
        return new User(MAIL, PASSWORD, role);
    }

    public static User createUser(PasswordEncoder passwordEncoder) {
        return new User(MAIL, passwordEncoder.encode(PASSWORD), User.Role.COMMON);
    }

    public static User createUser(String mail) {
        return new User(mail, PASSWORD, User.Role.COMMON);
    }

    public static User createUser(String mail, String password) {
        return new User(mail, password, User.Role.COMMON);
    }

    public static User createUser(UUID passwordResetToken) {
        var user = new User(MAIL, PASSWORD, User.Role.COMMON);
        user.assignPasswordResetToken(passwordResetToken);
        return user;
    }
}
