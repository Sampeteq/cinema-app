package com.cinema.users.infrastructure;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record UserCreateDto(
        @NotBlank
        @Email
        String mail,
        @NotBlank
        @Size(min = 5, max = 20)
        String password
) {
}
