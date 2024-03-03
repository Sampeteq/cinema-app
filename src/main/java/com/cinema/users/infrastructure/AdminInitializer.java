package com.cinema.users.infrastructure;

import com.cinema.users.domain.UserService;
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
    private final UserService userService;

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        userService.createAdmin(adminProperties.mail(), adminProperties.password());
    }
}
@ConfigurationProperties(prefix = "admin")
record AdminProperties(String mail, String password) {}
