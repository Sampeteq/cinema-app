package com.cinema.users.application.services;

import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.properties.AdminProperties;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
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
class UserAdminService {

    private final AdminProperties adminProperties;
    private final UserService userService;

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        try {
            var userCreateDto = new UserCreateDto(
                    adminProperties.getMail(),
                    adminProperties.getPassword(),
                    adminProperties.getPassword()
            );
            userService.createAdmin(userCreateDto);
            log.info("Admin added");
        } catch (UserMailAlreadyExistsException exception) {
            log.info("Admin already exists");
        }
    }
}
