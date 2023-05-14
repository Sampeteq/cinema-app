package code.utils;

import code.user.application.commands.SignInCommand;
import code.user.application.commands.SignUpCommand;
import code.user.domain.User;
import code.user.domain.UserRole;

public class UserTestHelper {

    public static SignUpCommand createSignUpCommand() {
        return new SignUpCommand(
                "user1@mail.com",
                "password1",
                "password1"
        );
    }

    public static SignUpCommand createSignUpCommand(String username) {
        return new SignUpCommand(
                username,
                "password1",
                "password1"
        );
    }

    public static SignUpCommand createSignUpCommand(String password, String repeatedPassword) {
        return new SignUpCommand(
                "user1@mail.com",
                password,
                repeatedPassword
        );
    }

    public static SignInCommand createSignInCommand(String mail) {
        return new SignInCommand(mail, "12345");
    }

    public static User createUser(String mail) {
        return User
                .builder()
                .mail(mail)
                .password("12345")
                .role(UserRole.COMMON)
                .build();
    }
}
