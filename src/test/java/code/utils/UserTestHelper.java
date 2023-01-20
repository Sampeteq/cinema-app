package code.utils;

import code.user.UserFacade;
import code.user.dto.SignUpDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserTestHelper {

    private final UserFacade userFacade;

    public static SignUpDto createSignUpDto() {
        return new SignUpDto(
                "user1",
                "password1",
                "password1"
        );
    }

    public static SignUpDto createSignUpDto(String username) {
        return new SignUpDto(
                username,
                "password1",
                "password1"
        );
    }

    public static SignUpDto createSignUpDto(String password, String repeatedPassword) {
        return new SignUpDto(
                "user1",
                password,
                repeatedPassword
        );
    }

    public String signUpUser() {
        var signUpRequest = createSignUpDto();
        userFacade.signUp(signUpRequest);
        return signUpRequest.username();
    }
}
