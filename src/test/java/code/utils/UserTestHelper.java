package code.utils;

import code.user.application.UserFacade;
import code.user.application.dto.SignUpDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserTestHelper {

    private final UserFacade userFacade;

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
        var signUpRequest = sampleSignUpDto(username);
        userFacade.signUp(signUpRequest);
        return signUpRequest.username();
    }
}
