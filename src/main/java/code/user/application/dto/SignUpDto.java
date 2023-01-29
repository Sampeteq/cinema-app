package code.user.application.dto;

import javax.validation.constraints.Size;

public record SignUpDto(

        @Size(min = 3, max = 15)
        String username,

        @Size(min = 5, max = 20)
        String password,

        @Size(min = 5, max = 20)
        String repeatedPassword
) {
}
