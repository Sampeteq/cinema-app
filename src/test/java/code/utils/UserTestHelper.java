package code.utils;

import code.user.client.commands.SignUpCommand;
import code.user.domain.User;
import code.user.domain.UserRole;

public class UserTestHelper {

    public static SignUpCommand createSignUpCommand() {
        return new SignUpCommand(
                "user1",
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
                "user1",
                password,
                repeatedPassword
        );
    }

    public static User createUser(String username) {
        return User
                .builder()
                .username(username)
                .password("12345")
                .role(UserRole.COMMON)
                .build();
    }
}
