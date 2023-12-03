package com.cinema.users.application.commands.handlers;

import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import com.cinema.users.domain.UserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateAdminHandler {

    private final UserFactory userFactory;
    private final UserRepository userRepository;

    public void handle(CreateAdmin command) {
        log.info("Command:{}", command);
        try {
            var admin = userFactory.createUser(command.adminMail(), command.adminPassword(), UserRole.ADMIN);
            userRepository.add(admin);
            log.info("Admin added");
        } catch (UserMailNotUniqueException exception) {
            log.info("Admin already exists");
        }
    }
}
