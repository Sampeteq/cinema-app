package com.cinema.users;

import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRole;

import java.util.UUID;

public final class UserFixture {

    private static final String MAIL = "user1@mail.com";

    private static final String PASSWORD = "password1";

    private UserFixture() {
    }

    public static CreateUser createCrateUserCommand() {
        return new CreateUser(
                MAIL,
                PASSWORD
        );
    }

    public static CreateAdmin createCrateAdminCommand() {
        return new CreateAdmin(
                MAIL,
                PASSWORD
        );
    }

    public static CreateUser createCrateUserCommand(String mail) {
        return new CreateUser(
                mail,
                PASSWORD
        );
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
