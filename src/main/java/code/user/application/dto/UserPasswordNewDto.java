package code.user.application.dto;

import java.util.UUID;

public record UserPasswordNewDto(
        UUID passwordResetToken,
        String newPassword
) {
}
