package code.utils;

import code.user.UserFacade;
import code.user.dto.SignUpRequest;

public class UserTestUtils {

    public static String signUpUser(UserFacade userFacade) {
        var signUpRequest = createSignUpRequest();
        userFacade.signUp(signUpRequest);
        return signUpRequest.username();
    }

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
}
