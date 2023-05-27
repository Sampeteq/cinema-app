package code.utils;

import code.user.application.commands.UserLoginCommand;
import code.user.application.commands.UserSignUpCommand;
import code.user.domain.User;
import code.user.domain.UserRole;

public class UserTestHelper {

    public static UserSignUpCommand createSignUpCommand() {
        return new UserSignUpCommand(
                "user1@mail.com",
                "password1",
                "password1"
        );
    }

    public static UserSignUpCommand createSignUpCommand(String username) {
        return new UserSignUpCommand(
                username,
                "password1",
                "password1"
        );
    }

    public static UserSignUpCommand createSignUpCommand(String password, String repeatedPassword) {
        return new UserSignUpCommand(
                "user1@mail.com",
                password,
                repeatedPassword
        );
    }

    public static UserLoginCommand createSignInCommand(String mail) {
        return new UserLoginCommand(mail, "12345");
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
