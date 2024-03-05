package com.cinema.users.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    public void assignPasswordResetToken(UUID passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
}
