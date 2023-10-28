package com.cinema.users.application.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.With;

@With
public record CreateUser(

        @NotBlank
        @Email
        String mail,

        @NotBlank
        @Size(min = 5, max = 20)
        String password
) {
}
