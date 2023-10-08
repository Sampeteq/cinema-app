package com.cinema.users;

import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRole;

import java.util.UUID;

public final class UserTestHelper {

    private static final String MAIL = "user1@mail.com";

    private static final String PASSWORD = "password1";

    private UserTestHelper() {
    }

    public static UserCreateDto createUserCreateDto() {
        return new UserCreateDto(
                MAIL,
                PASSWORD,
                PASSWORD
        );
    }

    public static UserCreateDto createUserCreateDto(String mail) {
        return createUserCreateDto().withMail(mail);
    }

    public static UserCreateDto createUserCreateDto(String password, String repeatedPassword) {
        return createUserCreateDto()
                .withPassword(password)
                .withRepeatedPassword(repeatedPassword);
    }

    public static User createUser() {
        return new User(MAIL, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(String mail) {
        return new User(mail, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(UUID passwordResetToken) {
        var user = new User(MAIL, PASSWORD, UserRole.COMMON);
        user.setPasswordResetToken(passwordResetToken);
        return user;
    }
}
