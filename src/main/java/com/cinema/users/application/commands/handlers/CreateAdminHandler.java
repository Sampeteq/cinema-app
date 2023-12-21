package com.cinema.users.application.commands.handlers;

import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.domain.UserFactory;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateAdminHandler {

    private final UserFactory userFactory;
    private final UserRepository userRepository;

    public void handle(CreateUser command) {
        if (!userRepository.existsByMail(command.mail())) {
            var admin = userFactory.createUser(command.mail(), command.password(), UserRole.ADMIN);
            userRepository.add(admin);
            log.info("Admin added");
        }
    }
}
