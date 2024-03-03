package com.cinema.users.infrastructure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

record UserNewPasswordRequest(
        @NotNull
        UUID passwordResetToken,

        @NotBlank
        @Size(min = 5, max = 20)
        String newPassword
) {
}
