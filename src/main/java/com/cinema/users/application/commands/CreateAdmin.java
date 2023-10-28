package com.cinema.users.application.commands;

public record CreateAdmin(
        String adminMail,
        String adminPassword
) {
}
