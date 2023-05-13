package code.user.client.commands;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record SignUpCommand(
        @Email
        String mail,

        @Size(min = 5, max = 20)
        String password,

        @Size(min = 5, max = 20)
        String repeatedPassword
) {
}
