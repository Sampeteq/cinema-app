package code.user.helpers;

import code.user.application.dto.UserSignInDto;
import code.user.application.dto.UserSignUpDto;
import code.user.domain.User;
import code.user.domain.UserRole;

import java.util.UUID;

public final class UserTestHelper {

    private UserTestHelper() {
    }

    public static UserSignUpDto createSignUpDto() {
        var mail = "user1@mail.com";
        var password = "password1";
        return new UserSignUpDto(
                mail,
                password,
                password
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

    public static UserSignInDto createSignInDto(String mail) {
        var password = "12345";
        return new UserSignInDto(mail, password);
    }

    public static User createUser() {
        var password = "12345";
        var mail = "user1@mail.com";
        return User.create(mail, password, UserRole.COMMON);
    }

    public static User createUser(String mail) {
        var password = "12345";
        return User.create(mail, password, UserRole.COMMON);
    }

    public static User createUser(UUID passwordResetToken) {
        var password = "12345";
        var mail = "user1@mail.com";
        var user = User.create(mail, password, UserRole.COMMON);
        user.setPasswordResetToken(passwordResetToken);
        return user;
    }
}
