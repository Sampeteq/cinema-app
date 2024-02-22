package com.cinema.users;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(schema = "public")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mail;

    private String password;

    public enum Role {
        COMMON,
        ADMIN
    }

    @Enumerated(value = EnumType.STRING)
    private User.Role role;

    private UUID passwordResetToken;

    protected User() {
    }

    public User(String mail, String password, Role role) {
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public void setNewPassword(String newPassword) {
        this.password = newPassword;
        this.passwordResetToken = null;
    }

    public void setPasswordResetToken(UUID passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
}
