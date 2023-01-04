package code.user;

import code.user.dto.SignUpDto;

public class UserTestUtils {

    public static String addSampleUser(UserFacade userFacade) {
        var sampleDto = sampleSignUpDto();
        userFacade.signUp(sampleDto);
        return sampleDto.username();
    }

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
}
