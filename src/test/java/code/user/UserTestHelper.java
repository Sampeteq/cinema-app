package code.user;

import code.user.application.dto.UserSignUpDto;
import code.user.domain.User;
import code.user.domain.UserRole;
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
        return User.create(MAIL, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(String mail) {
        return User.create(mail, PASSWORD, UserRole.COMMON);
    }

    public static User createUser(String mail, String password, PasswordEncoder passwordEncoder) {
        return User.create(mail, passwordEncoder.encode(password), UserRole.COMMON);
    }

    public static User createUser(UUID passwordResetToken) {
        var user = User.create(MAIL, PASSWORD, UserRole.COMMON);
        user.setPasswordResetToken(passwordResetToken);
        return user;
    }
}
