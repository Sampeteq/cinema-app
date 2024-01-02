package com.cinema.users.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank
        @Email
        String mail,
        @NotBlank
        @Size(min = 5, max = 20)
        String password
) {
}
