package com.cinema.users.infrastructure.initializer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "user.admin")
public record UserAdminProperties(String mail, String password) {}
