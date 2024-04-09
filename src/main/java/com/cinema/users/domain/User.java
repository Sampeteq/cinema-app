package com.cinema.users.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class User {
    private UUID id;
    private String mail;
    private String password;
    private UserRole role;
    private UUID passwordResetToken;

    public void setNewPassword(String newPassword) {
        this.password = newPassword;
        this.passwordResetToken = null;
    }

    public void assignPasswordResetToken(UUID passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
}
