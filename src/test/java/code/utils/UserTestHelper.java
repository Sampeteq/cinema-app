package code.utils;

import code.user.UserFacade;
import code.user.dto.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserTestHelper {

    private final UserFacade userFacade;

    public static SignUpRequest createSignUpRequest() {
        return new SignUpRequest(
                "user1",
                "password1",
                "password1"
        );
    }

    public static SignUpRequest createSignUpRequest(String username) {
        return new SignUpRequest(
                username,
                "password1",
                "password1"
        );
    }

    public static SignUpRequest createSignUpRequest(String password, String repeatedPassword) {
        return new SignUpRequest(
                "user1",
                password,
                repeatedPassword
        );
    }

    public String signUpUser() {
        var signUpRequest = createSignUpRequest();
        userFacade.signUp(signUpRequest);
        return signUpRequest.username();
    }
}
