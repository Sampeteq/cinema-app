package com.cinema.users.application.services;

import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@Slf4j
class UserAdminService {

    private final String adminMail;
    private final String adminPassword;
    private final UserService userService;

    public UserAdminService(
            @Value("${admin.mail}") String adminMail,
            @Value("${admin.password}") String adminPassword,
            UserService userService
    ) {
        this.userService = userService;
        this.adminMail = adminMail;
        this.adminPassword = adminPassword;
    }

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        try {
            var userCreateDto = new UserCreateDto(adminMail, adminPassword, adminPassword);
            userService.createAdmin(userCreateDto);
            log.info("Admin added");
        } catch (UserMailAlreadyExistsException exception) {
            log.info("Admin already exists");
        }
    }
}
