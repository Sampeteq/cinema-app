package com.cinema.users.infrastrcuture;

import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.application.commands.handlers.CreateAdminHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
class AdminInitializer {

    private final AdminProperties adminProperties;
    private final CreateAdminHandler createAdminHandler;

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        var command = new CreateUser(adminProperties.mail(), adminProperties.password());
        createAdminHandler.handle(command);
    }
}
@ConfigurationProperties(prefix = "admin")
record AdminProperties(String mail, String password) {}
