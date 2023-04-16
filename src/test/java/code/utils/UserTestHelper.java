package code.utils;

import code.user.domain.commands.SignUpCommand;
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
        return new User(username, "12345", UserRole.COMMON);
    }
}
