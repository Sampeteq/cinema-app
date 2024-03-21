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
class UserInitializer {

    private final UserAdminProperties userAdminProperties;
    private final UserService userService;

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        userService.createAdmin(userAdminProperties.mail(), userAdminProperties.password());
    }
}
@ConfigurationProperties(prefix = "user.admin")
record UserAdminProperties(String mail, String password) {}
