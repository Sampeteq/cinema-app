package com.cinema.user;

import com.cinema.user.application.dto.UserSignUpDto;
import com.cinema.user.domain.User;
import com.cinema.user.domain.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public final class UserTestHelper {

    private static final String MAIL = "user1@mail.com";

    private static final String PASSWORD = "password1";

    private UserTestHelper() {
    }

    public static UserSignUpDto createSignUpDto() {
        return new UserSignUpDto(
                MAIL,
                PASSWORD,
                PASSWORD
        );
    }

    public static UserSignUpDto createSignUpDto(String mail) {
        return createSignUpDto().withMail(mail);
    }

    public static UserSignUpDto createSignUpDto(String password, String repeatedPassword) {
        return createSignUpDto()
                .withPassword(password)
                .withRepeatedPassword(repeatedPassword);
    }

    public static User createUser() {
        return new User(MAIL, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(String mail) {
        return new User(mail, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(String mail, String password, PasswordEncoder passwordEncoder) {
        return new User(mail, passwordEncoder.encode(password), UserRole.COMMON);
    }

    public static User createUser(UUID passwordResetToken) {
        var user = new User(MAIL, PASSWORD, UserRole.COMMON);
        user.setPasswordResetToken(passwordResetToken);
        return user;
    }
}
