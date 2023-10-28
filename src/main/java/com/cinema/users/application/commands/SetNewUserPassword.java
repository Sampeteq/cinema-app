package com.cinema.users.application.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SetNewUserPassword(
        @NotNull
        UUID passwordResetToken,

        @NotBlank
        @Size(min = 5, max = 20)
        String newPassword
) {
}
