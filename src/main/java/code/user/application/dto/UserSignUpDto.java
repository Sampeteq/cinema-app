package code.user.application.dto;

import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@With
public record UserSignUpDto(

        @NotBlank
        @Email
        String mail,

        @NotBlank
        @Size(min = 5, max = 20)
        String password,

        @NotBlank
        @Size(min = 5, max = 20)
        String repeatedPassword
) {
}
