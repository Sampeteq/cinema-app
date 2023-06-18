package code.user.helpers;

import code.user.application.dto.UserSignInDto;
import code.user.application.dto.UserSignUpDto;
import code.user.domain.User;
import code.user.domain.UserRole;

public class UserTestHelper {

    public static UserSignUpDto createSignUpDto() {
        return new UserSignUpDto(
                "user1@mail.com",
                "password1",
                "password1"
        );
    }

    public static UserSignUpDto createSignUpDto(String username) {
        return new UserSignUpDto(
                username,
                "password1",
                "password1"
        );
    }

    public static UserSignUpDto createSignUpDto(String password, String repeatedPassword) {
        return new UserSignUpDto(
                "user1@mail.com",
                password,
                repeatedPassword
        );
    }

    public static UserSignInDto createSignInCommand(String mail) {
        return new UserSignInDto(mail, "12345");
    }

    public static User createUser(String mail) {
        return new User(mail, "12345", UserRole.COMMON);
    }
}
