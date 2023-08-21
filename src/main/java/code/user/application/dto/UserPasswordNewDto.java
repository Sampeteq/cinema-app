package code.user.application.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public record UserPasswordNewDto(
        @NotNull
        UUID passwordResetToken,

        @NotBlank
        @Size(min = 5, max = 20)
        String newPassword
) {
}
