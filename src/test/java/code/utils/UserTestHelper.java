package code.utils;

import code.user.application.dto.SignUpDto;
import code.user.domain.User;
import code.user.domain.UserRepository;
import code.user.domain.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserTestHelper {

    private final UserRepository userRepository;

    public static SignUpDto sampleSignUpDto() {
        return new SignUpDto(
                "user1",
                "password1",
                "password1"
        );
    }

    public static SignUpDto sampleSignUpDto(String username) {
        return new SignUpDto(
                username,
                "password1",
                "password1"
        );
    }

    public static SignUpDto sampleSignUpDto(String password, String repeatedPassword) {
        return new SignUpDto(
                "user1",
                password,
                repeatedPassword
        );
    }

    public String signUpUser(String username) {
        var user = new User(username, "12345", UserRole.COMMON);
        userRepository.save(user);
        return username;
    }
}
