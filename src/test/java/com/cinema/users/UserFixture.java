package com.cinema.users;

import com.cinema.users.application.dto.CreateUserDto;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public final class UserFixture {

    private static final String MAIL = "user1@mail.com";

    public static final String PASSWORD = "password1";

    private UserFixture() {
    }

    public static CreateUserDto createCrateUserDto() {
        return new CreateUserDto(
                MAIL,
                PASSWORD
        );
    }

    public static CreateUserDto createCrateUserDto(String mail) {
        return new CreateUserDto(
                mail,
                PASSWORD
        );
    }

    public static User createUser() {
        return new User(MAIL, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(UserRole role) {
        return new User(MAIL, PASSWORD, role);
    }

    public static User createUser(PasswordEncoder passwordEncoder) {
        return new User(MAIL, passwordEncoder.encode(PASSWORD), UserRole.COMMON);
    }

    public static User createUser(String mail) {
        return new User(mail, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(String mail, String password) {
        return new User(mail, password, UserRole.COMMON);
    }

    public static User createUser(UUID passwordResetToken) {
        var user = new User(MAIL, PASSWORD, UserRole.COMMON);
        user.setPasswordResetToken(passwordResetToken);
        return user;
    }
}
