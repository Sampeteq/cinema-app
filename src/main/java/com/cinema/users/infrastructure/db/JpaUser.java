package com.cinema.users.infrastructure.db;

import com.cinema.users.domain.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(schema = "public", name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class JpaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mail;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    private UUID passwordResetToken;
}
