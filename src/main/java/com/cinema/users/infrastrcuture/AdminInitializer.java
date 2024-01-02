package com.cinema.users.infrastrcuture;

import com.cinema.users.application.UserService;
import com.cinema.users.application.dto.CreateUserDto;
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
        var createUserDto = new CreateUserDto(adminProperties.mail(), adminProperties.password());
        userService.createAdmin(createUserDto);
    }
}
@ConfigurationProperties(prefix = "admin")
record AdminProperties(String mail, String password) {}
