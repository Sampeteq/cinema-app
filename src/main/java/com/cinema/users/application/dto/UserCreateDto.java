package com.cinema.users.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.With;

@With
public record UserCreateDto(

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
