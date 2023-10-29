package com.cinema.users.application.commands.handlers;

import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.infrastrcuture.properties.AdminProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
class CreateAdminOnStartUpHandler {

    private final AdminProperties adminProperties;
    private final CreateAdminHandler createAdminHandler;

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        var command = new CreateAdmin(adminProperties.getMail(), adminProperties.getPassword());
        createAdminHandler.handle(command);
    }
}
