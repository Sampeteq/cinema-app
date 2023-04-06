package code.user.domain.commands;

import javax.validation.constraints.Size;

public record SignUpCommand(

        @Size(min = 3, max = 15)
        String username,

        @Size(min = 5, max = 20)
        String password,

        @Size(min = 5, max = 20)
        String repeatedPassword
) {
}
