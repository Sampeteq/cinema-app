package com.cinema.users.infrastructure.initializer;

import com.cinema.users.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class UserInitializer {

    private final UserAdminProperties userAdminProperties;
    private final UserService userService;

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        userService.createAdmin(userAdminProperties.mail(), userAdminProperties.password());
    }
}
