package code.utils;

import code.user.client.commands.SignUpCommand;
import code.user.domain.User;
import code.user.domain.UserRole;

public class UserTestHelper {

    public static SignUpCommand createSampleSignUpCommand() {
        return new SignUpCommand(
                "user1",
                "password1",
                "password1"
        );
    }

    public static SignUpCommand createSampleSignUpCommand(String username) {
        return new SignUpCommand(
                username,
                "password1",
                "password1"
        );
    }

    public static SignUpCommand createSampleSignUpCommand(String password, String repeatedPassword) {
        return new SignUpCommand(
                "user1",
                password,
                repeatedPassword
        );
    }

    public static User createSampleUser(String username) {
        return User
                .builder()
                .username(username)
                .password("12345")
                .role(UserRole.COMMON)
                .build();
    }
}
