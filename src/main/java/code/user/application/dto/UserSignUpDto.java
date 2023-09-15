package code.user.application.dto;

import lombok.With;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
